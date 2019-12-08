package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.utils.AppLog

open class LoadUserUsecase(context: Context, private val scope: CoroutineScope) : AsyncUsecase<Unit, List<String>>(context, scope) {

    private val userRepository: UserRepository by inject()

    @WorkerThread
    override suspend fun callAsync(param: Unit): Deferred<List<String>> = scope.async(Dispatchers.IO) {
        AppLog.info(TAG, "callAsync")
        Thread.sleep(1000)
        return@async userRepository.getAllUser().map { "${it.id}: ${it.displayName}" }
    }

    companion object {
        private val TAG = LoadUserUsecase::class.java.simpleName
    }
}
