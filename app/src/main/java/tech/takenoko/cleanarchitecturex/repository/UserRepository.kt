package tech.takenoko.cleanarchitecturex.repository

import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource

interface UserRepository {
    suspend fun getAllUser(): List<UserLocalDataSource.User>
    suspend fun addUser(name: String)
}
