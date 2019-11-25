package tech.takenoko.cleanarchitecturex.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import tech.takenoko.cleanarchitecturex.model.UsecaseResult

/**
abstract class SyncUsecase<Q: Any, P: Any> {

    private var result = MediatorLiveData<UsecaseResult<P>>()
    val source: LiveData<UsecaseResult<P>> = result

    fun execute(param: Q) {
        result.postValue(UsecaseResult.Pending())
        runCatching {
            result.postValue(UsecaseResult.Resolved(call(param)))
        }.onFailure {
            result.postValue(UsecaseResult.Rejected(it))
        }
    }

    protected abstract fun call(param: Q): P

    companion object {
        val TAG = SyncUsecase::class.java.simpleName
    }
}
**/