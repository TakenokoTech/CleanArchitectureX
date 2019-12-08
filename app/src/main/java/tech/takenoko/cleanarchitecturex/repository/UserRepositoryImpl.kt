package tech.takenoko.cleanarchitecturex.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.entities.response.UserEntity
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.repository.remote.UserRemoteDataSource
import tech.takenoko.cleanarchitecturex.utils.AppLog

class UserRepositoryImpl : UserRepository, KoinComponent {

    private val local: UserLocalDataSource by inject()
    private val network: UserRemoteDataSource by inject()

    @WorkerThread
    override suspend fun getAllUser(): List<UserLocalDataSource.User> {
        AppLog.info(TAG, "getAllUser")
        val users = network.getUser().map {
            UserLocalDataSource.User(it.user_name, it.display_name)
        }
        local.deleteAll()
        local.insertAll(*users.toTypedArray())
        return local.getAll()
    }

    @WorkerThread
    override suspend fun addUser(user: UserLocalDataSource.User) {
        AppLog.info(TAG, "addUser")
        val result = network.postUser(user = UserEntity(user.userName, user.displayName))
        AppLog.info(TAG, "postUser ==> $result")
        return local.insertAll(user)
    }

    @MainThread
    override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> {
        return local.getAllToLive()
    }

    companion object {
        private val TAG = UserRepositoryImpl::class.java.simpleName
    }
}
