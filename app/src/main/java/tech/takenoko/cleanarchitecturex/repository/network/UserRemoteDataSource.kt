package tech.takenoko.cleanarchitecturex.repository.network

import android.content.Context
import androidx.annotation.WorkerThread

class UserRemoteDataSource(context: Context): BaseDataSource(context) {

    private val url = "https://aaaa"

    @WorkerThread
    suspend fun getUser(): List<String> {
        return awaitGet(url = url)
    }

    @WorkerThread
    suspend fun postUser() {
        return awaitPost(url = url, body = UserRequest("user1"))
    }
}

data class UserRequest(val name: String)