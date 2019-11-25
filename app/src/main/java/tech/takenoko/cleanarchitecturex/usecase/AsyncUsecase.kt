package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import tech.takenoko.cleanarchitecturex.model.UsecaseResult

abstract class AsyncUsecase<Q: Any, P: Any>(private val context: Context, private val viewModel: ViewModel): KoinComponent {

    private var result = MediatorLiveData<UsecaseResult<P>>()
    val source: LiveData<UsecaseResult<P>> = result

    fun execute(param: Q) {
        result.postValue(UsecaseResult.Pending())
        runCatching {
            viewModel.viewModelScope.launch {
                result.postValue(UsecaseResult.Resolved(callAsync(param).await()))
            }
        }.onFailure {
            result.postValue(UsecaseResult.Rejected(it))
        }
    }

    protected abstract suspend fun callAsync(param: Q): Deferred<P>

    companion object {
        val TAG = AsyncUsecase::class.java.simpleName
    }
}
