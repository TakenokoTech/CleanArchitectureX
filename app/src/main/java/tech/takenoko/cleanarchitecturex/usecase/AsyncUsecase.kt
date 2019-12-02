package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import tech.takenoko.cleanarchitecturex.entities.UsecaseResult
import tech.takenoko.cleanarchitecturex.utils.AppLog

abstract class AsyncUsecase<Q : Any, P : Any>(private val context: Context, private val scope: CoroutineScope) : KoinComponent {

    private var result = MediatorLiveData<UsecaseResult<P>>()
    val source: LiveData<UsecaseResult<P>> = result

    @MainThread
    open fun execute(param: Q) {
        result.postValue(UsecaseResult.Pending())
        scope.launch {
            runCatching {
                callAsync(param).await()
            }.fold({
                result.postValue(UsecaseResult.Resolved(it))
            }, {
                AppLog.warn(TAG, it)
                result.postValue(UsecaseResult.Rejected(it))
            })
        }
    }

    @WorkerThread
    protected abstract suspend fun callAsync(param: Q): Deferred<P>

    companion object {
        private val TAG = AsyncUsecase::class.java.simpleName
    }
}
