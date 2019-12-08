package tech.takenoko.cleanarchitecturex.repository

import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.mock
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.utils.AppLog

class MockUserRepository : UserRepository {
    override suspend fun getAllUser(): List<UserLocalDataSource.User> = allUser
    override suspend fun addUser(user: UserLocalDataSource.User) {
        AppLog.debug("MockUserRepository", "$user")
    }
    override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> = mock {}

    companion object {
        var allUser: List<UserLocalDataSource.User> = listOf()
    }
}
