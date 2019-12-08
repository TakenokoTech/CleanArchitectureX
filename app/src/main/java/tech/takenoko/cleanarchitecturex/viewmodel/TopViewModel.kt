package tech.takenoko.cleanarchitecturex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.entities.isLoading
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.usecase.RegisterUserUsecase
import tech.takenoko.cleanarchitecturex.utils.AppLog

class TopViewModel : ViewModel(), KoinComponent {

    private val registerUserUsecase: RegisterUserUsecase by inject { parametersOf(viewModelScope) }
    private val loadUserUsecase: LoadUserUsecase by inject { parametersOf(viewModelScope) }

    private val _userNameList: MediatorLiveData<List<String>> = MediatorLiveData()
    val userNameList: LiveData<List<String>> = _userNameList

    private val _isLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage: MediatorLiveData<String?> = MediatorLiveData()
    val errorMessage: LiveData<String?> = _errorMessage
    fun resetErrorMessage() { _errorMessage.value = null }

    init {
        _userNameList.addSource(loadUserUsecase.source) { loadUserUsecaseHandlerToList(it) }
        _isLoading.addSource(loadUserUsecase.source) { usecaseHandlerIsLoading() }
        _isLoading.addSource(registerUserUsecase.source) { usecaseHandlerIsLoading().run { registerUserUsecaseHandler(it) } }
    }

    fun load() {
        AppLog.info(TAG, "load")
        loadUserUsecase.execute(Unit)
    }

    fun register() {
        AppLog.info(TAG, "register")
        val user = UserLocalDataSource.User("UserName", "DisplayName")
        val param = RegisterUserUsecase.Param(user)
        registerUserUsecase.execute(param)
    }

    private fun loadUserUsecaseHandlerToList(result: UsecaseResult<List<String>>): Any = when (result) {
        is UsecaseResult.Pending -> Unit
        is UsecaseResult.Resolved -> _userNameList.value = result.value
        is UsecaseResult.Rejected -> _errorMessage.value = result.reason.localizedMessage
    }

    private fun registerUserUsecaseHandler(result: UsecaseResult<Unit>): Any = when (result) {
        is UsecaseResult.Pending -> Unit
        is UsecaseResult.Resolved -> load()
        is UsecaseResult.Rejected -> _errorMessage.value = result.reason.localizedMessage
    }

    private fun usecaseHandlerIsLoading() {
        val registerUserUsecaseIsLoading = registerUserUsecase.source.value.isLoading()
        val loadUserUsecaseLoading = loadUserUsecase.source.value.isLoading()
        _isLoading.value = registerUserUsecaseIsLoading || loadUserUsecaseLoading
    }

    companion object {
        private val TAG = TopViewModel::class.java.simpleName
    }
}
