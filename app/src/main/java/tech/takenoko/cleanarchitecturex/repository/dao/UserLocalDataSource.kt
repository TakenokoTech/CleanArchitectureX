package tech.takenoko.cleanarchitecturex.repository.dao

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserLocalDataSource(context: Context) {

    private val database = AppDatabase.getDatabase(context)

    @WorkerThread
    suspend fun getAll(): List<User> {
        return database.userDao().getAll()
    }

    @WorkerThread
    suspend fun insertAll(vararg users: User) {
        database.userDao().insertAll(*users)
    }

    @WorkerThread
    suspend fun deleteAll() {
        database.userDao().deleteAll()
    }

    @WorkerThread
    fun getAllToLive(): LiveData<List<User>> {
        return database.userDao().getAllToLive()
    }
}