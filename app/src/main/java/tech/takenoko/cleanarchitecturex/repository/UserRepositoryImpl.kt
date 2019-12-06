package tech.takenoko.cleanarchitecturex.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import java.util.UUID
import org.koin.core.KoinComponent
import org.koin.core.inject
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
            UserLocalDataSource.User(
                UUID.randomUUID().toString(),
                it.name
            )
        }
        local.deleteAll()
        local.insertAll(*users.toTypedArray())
        return local.getAll()
    }

    @WorkerThread
    override suspend fun addUser(name: String) {
        AppLog.info(TAG, "addUser")
        val result = network.postUser()
        AppLog.info(TAG, "postUser ==> $result")
        return local.insertAll(
            UserLocalDataSource.User(
                UUID.randomUUID().toString(),
                name
            )
        )
    }

    @MainThread
    override fun getAllToLive(): LiveData<List<UserLocalDataSource.User>> {
        return local.getAllToLive()
    }

    companion object {
        private val TAG = UserRepositoryImpl::class.java.simpleName
    }
}
