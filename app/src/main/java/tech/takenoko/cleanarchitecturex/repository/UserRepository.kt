package tech.takenoko.cleanarchitecturex.repository

import tech.takenoko.cleanarchitecturex.repository.local.User

interface UserRepository {
    suspend fun getAllUser(): List<User>
    suspend fun addUser(name: String)
}
