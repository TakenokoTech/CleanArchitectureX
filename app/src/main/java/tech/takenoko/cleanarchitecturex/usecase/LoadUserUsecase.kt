package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import tech.takenoko.cleanarchitecturex.utils.AppLog
import java.util.*

class LoadUserUsecase(private val context: Context, private val scope: CoroutineScope): AsyncUsecase<Unit, List<String>>(context, scope) {

    private val userRepository: UserRepository by inject()

    override suspend fun callAsync(param: Unit): Deferred<List<String>> = scope.async(Dispatchers.IO) {
        AppLog.info(TAG,"callAsync")
        Thread.sleep(1000)

        userRepository.addUser(UUID.randomUUID().toString())
        val result = userRepository.getAllUser().mapNotNull { it.name }

        return@async result
    }

    companion object {
        val TAG = LoadUserUsecase::class.java.simpleName
    }
}