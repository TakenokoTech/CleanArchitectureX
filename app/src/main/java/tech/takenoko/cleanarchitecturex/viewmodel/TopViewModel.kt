package tech.takenoko.cleanarchitecturex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import tech.takenoko.cleanarchitecturex.model.UsecaseResult
import tech.takenoko.cleanarchitecturex.usecase.LoadUserUsecase

class TopViewModel : BaseViewModel() {

    private val loadUserUsecase: LoadUserUsecase by inject { parametersOf(this) }

    private val _text1: MediatorLiveData<String> = MediatorLiveData()
    val text1: LiveData<String> = _text1

    init {
        _text1.addSource(loadUserUsecase.source) { loadUserUsecaseHandler(it) }
    }

    fun load() {
        loadUserUsecase.execute(Unit)
    }

    private fun loadUserUsecaseHandler(result: UsecaseResult<String>): Unit = when(result) {
        is UsecaseResult.Pending -> _text1.value = "loading..."
        is UsecaseResult.Resolved -> _text1.value = result.value
        is UsecaseResult.Rejected -> _text1.value = result.reason.message
    }
}