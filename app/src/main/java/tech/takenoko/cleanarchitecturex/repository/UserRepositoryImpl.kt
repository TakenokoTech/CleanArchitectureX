package tech.takenoko.cleanarchitecturex.repository

import android.content.Context
import tech.takenoko.cleanarchitecturex.repository.dao.AppDatabase
import tech.takenoko.cleanarchitecturex.repository.dao.User
import tech.takenoko.cleanarchitecturex.utils.AppLog
import java.util.*

class UserRepositoryImpl(context: Context): UserRepository {

    // private val database by lazy { FirebaseDatabase.getInstance().reference }
    private val database = AppDatabase.getDatabase(context)


    override suspend fun getAllUser(): List<User> {
        AppLog.info(TAG, "getAllUser")
        return database.userDao().getAll()
    }

    override suspend fun addUser(name: String) {
        AppLog.info(TAG, "addUser")
        return database.userDao().insertAll(User(UUID.randomUUID().toString(), "name"))
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.simpleName
    }
}