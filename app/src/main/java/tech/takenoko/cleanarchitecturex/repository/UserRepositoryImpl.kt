package tech.takenoko.cleanarchitecturex.repository

import android.content.Context
import androidx.annotation.WorkerThread
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.repository.local.User
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource
import tech.takenoko.cleanarchitecturex.repository.network.UserRemoteDataSource
import tech.takenoko.cleanarchitecturex.utils.AppLog
import java.util.*

class UserRepositoryImpl(context: Context): UserRepository, KoinComponent {

    // private val database by lazy { FirebaseDatabase.getInstance().reference }
    private val local: UserLocalDataSource by inject()
    private val network: UserRemoteDataSource by inject()

    @WorkerThread
    override suspend fun getAllUser(): List<User> {
        AppLog.info(TAG, "getAllUser")
        val users = network.getUser().map { User(UUID.randomUUID().toString(), it.name) }
        local.deleteAll()
        local.insertAll(*users.toTypedArray())
        return local.getAll()
    }

    @WorkerThread
    override suspend fun addUser(name: String) {
        AppLog.info(TAG, "addUser")
        network.postUser()
        return local.insertAll(User(UUID.randomUUID().toString(), name))
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.simpleName
    }
}