package tech.takenoko.cleanarchitecturex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.entities.isLoading
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.usecase.RegisterUserUsecase
import tech.takenoko.cleanarchitecturex.utils.AppLog

class TopViewModel : BaseViewModel() {

    private val registerUserUsecase: RegisterUserUsecase by inject { parametersOf(viewModelScope) }
    private val loadUserUsecase: LoadUserUsecase by inject { parametersOf(viewModelScope) }

    private val _text1: MediatorLiveData<String> = MediatorLiveData()
    val text1: LiveData<String> = _text1

    private val _list1: MediatorLiveData<List<String>> = MediatorLiveData()
    val list1: LiveData<List<String>> = _list1

    private val _isLoading: MediatorLiveData<Boolean> = MediatorLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _text1.addSource(loadUserUsecase.source) { loadUserUsecaseHandlerToText(it) }
        _list1.addSource(loadUserUsecase.source) { loadUserUsecaseHandlerToList(it) }

        _isLoading.addSource(loadUserUsecase.source) { usecaseHandlerIsLoading() }
        _isLoading.addSource(registerUserUsecase.source) { usecaseHandlerIsLoading().run { registerUserUsecaseHandler(it) } }
    }

    fun load() {
        AppLog.info(TAG, "load")
        loadUserUsecase.execute(Unit)
    }

    fun register() {
        AppLog.info(TAG, "register")
        registerUserUsecase.execute(Unit)
    }

    private fun loadUserUsecaseHandlerToText(result: UsecaseResult<List<String>>): Unit = when (result) {
        is UsecaseResult.Pending -> _text1.value = "loading..."
        is UsecaseResult.Resolved -> _text1.value = "success!"
        is UsecaseResult.Rejected -> _text1.value = result.reason.message
    }

    private fun loadUserUsecaseHandlerToList(result: UsecaseResult<List<String>>): Unit = when (result) {
        is UsecaseResult.Pending -> Unit
        is UsecaseResult.Resolved -> _list1.value = result.value
        is UsecaseResult.Rejected -> Unit
    }

    private fun registerUserUsecaseHandler(result: UsecaseResult<Unit>): Unit = when (result) {
        is UsecaseResult.Pending -> Unit
        is UsecaseResult.Resolved -> load()
        is UsecaseResult.Rejected -> Unit
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
