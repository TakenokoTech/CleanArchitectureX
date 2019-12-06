package tech.takenoko.cleanarchitecturex.repository

import androidx.lifecycle.LiveData
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource

interface UserRepository {
    suspend fun getAllUser(): List<UserLocalDataSource.User>
    suspend fun addUser(name: String)

    fun getAllToLive(): LiveData<List<UserLocalDataSource.User>>
}
