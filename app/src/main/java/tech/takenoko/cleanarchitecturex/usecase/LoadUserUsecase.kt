package tech.takenoko.cleanarchitecturex.usecase

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.repository.UserRepository
import java.util.*

class LoadUserUsecase(private val context: Context, private val viewModel: ViewModel): AsyncUsecase<Unit, String>(context, viewModel) {

    val userRepository: UserRepository by inject()

    override suspend fun callAsync(param: Unit): Deferred<String> = viewModel.viewModelScope.async(Dispatchers.IO) {
        Log.i(TAG,"call.")
        Thread.sleep(2000)
        userRepository.addUser(UUID.randomUUID().toString())
        val result = userRepository.getAllUser().mapNotNull { it.name }
        return@async result.firstOrNull() ?: ""
    }

    companion object {
        val TAG = LoadUserUsecase::class.java.simpleName
    }
}