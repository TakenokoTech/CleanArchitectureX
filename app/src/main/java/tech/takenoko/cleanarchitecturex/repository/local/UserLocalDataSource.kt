package tech.takenoko.cleanarchitecturex.repository.local

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.takenoko.cleanarchitecturex.di.AppDatabase
import tech.takenoko.cleanarchitecturex.entities.room.UserDao

class UserLocalDataSource(context: Context) :
    UserDao {

    private val database = AppDatabase.getDatabase(context)

    @WorkerThread
    override suspend fun getAll(): List<User> {
        return database.userDao().getAll()
    }

    override suspend fun findByName(name: String): User {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    @WorkerThread
    override suspend fun insertAll(vararg users: User) {
        database.userDao().insertAll(*users)
    }

    override suspend fun delete(user: User) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    @WorkerThread
    override suspend fun deleteAll() {
        database.userDao().deleteAll()
    }

    @WorkerThread
    override fun getAllToLive(): LiveData<List<User>> {
        return database.userDao().getAllToLive()
    }

    @Entity
    data class User(
        @PrimaryKey val uid: String,
        @ColumnInfo(name = "name") val name: String?
    )
}
