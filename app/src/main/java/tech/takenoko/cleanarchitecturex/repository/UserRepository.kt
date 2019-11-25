package tech.takenoko.cleanarchitecturex.repository

import tech.takenoko.cleanarchitecturex.repository.dao.User

interface UserRepository {
    suspend fun getAllUser(): List<User>
    suspend fun addUser(name: String)
}