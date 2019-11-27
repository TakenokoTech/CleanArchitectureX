package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import tech.takenoko.cleanarchitecturex.model.UsecaseResult
import tech.takenoko.cleanarchitecturex.utils.AppLog

abstract class AsyncUsecase<Q: Any, P: Any>(private val context: Context, private val scope: CoroutineScope): KoinComponent {

    private var result = MediatorLiveData<UsecaseResult<P>>()
    val source: LiveData<UsecaseResult<P>> = result

    @MainThread
    fun execute(param: Q) {
        result.postValue(UsecaseResult.Pending())
        scope.launch {
            runCatching {
                callAsync(param).await()
            }.fold({
                result.postValue(UsecaseResult.Resolved(it))
            }, {
                AppLog.warn(TAG,it)
                result.postValue(UsecaseResult.Rejected(it))
            })
        }
    }

    @WorkerThread
    protected abstract suspend fun callAsync(param: Q): Deferred<P>

    companion object {
        val TAG = AsyncUsecase::class.java.simpleName
    }
}
