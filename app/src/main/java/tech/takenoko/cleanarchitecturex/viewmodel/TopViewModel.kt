package tech.takenoko.cleanarchitecturex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase
import tech.takenoko.cleanarchitecturex.utils.AppLog

class TopViewModel : BaseViewModel() {

    private val loadUserUsecase: LoadUserUsecase by inject { parametersOf(viewModelScope) }

    private val _text1: MediatorLiveData<String> = MediatorLiveData()
    val text1: LiveData<String> = _text1

    private val _list1: MediatorLiveData<List<String>> = MediatorLiveData()
    val list1: LiveData<List<String>> = _list1

    init {
        _text1.addSource(loadUserUsecase.source) { loadUserUsecaseHandlerToText(it) }
        _list1.addSource(loadUserUsecase.source) { loadUserUsecaseHandlerToList(it) }
    }

    fun load() {
        AppLog.info(TAG, "load")
        loadUserUsecase.execute(Unit)
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

    companion object {
        private val TAG = TopViewModel::class.java.simpleName
    }
}
