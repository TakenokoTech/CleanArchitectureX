package tech.takenoko.cleanarchitecturex.repository

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import tech.takenoko.cleanarchitecturex.repository.dao.AppDatabase
import tech.takenoko.cleanarchitecturex.repository.dao.User
import java.util.*

class UserRepositoryImpl(context: Context): UserRepository {

    // private val database by lazy { FirebaseDatabase.getInstance().reference }
    private val database = AppDatabase.getDatabase(context)


    override suspend fun getAllUser(): List<User> {
        return database.userDao().getAll()
    }

    override suspend fun addUser(name: String) {
        return database.userDao().insertAll(User(UUID.randomUUID().toString(), "name"))
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.simpleName
    }
}