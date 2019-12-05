package tech.takenoko.cleanarchitecturex.repository.local

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.takenoko.cleanarchitecturex.di.AppDatabase
import tech.takenoko.cleanarchitecturex.entities.room.UserDao
import tech.takenoko.cleanarchitecturex.utils.AppLog

class UserLocalDataSource : UserDao, KoinComponent {

    private val database: AppDatabase by inject()

    @WorkerThread
    override suspend fun getAll(): List<User> {
        return database.userDao().getAll()
    }

    @WorkerThread
    override suspend fun insertAll(vararg users: User) {
        AppLog.debug(TAG, "${users.map { it.uid }}")
        database.userDao().insertAll(*users)
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

    companion object {
        val TAG = UserLocalDataSource::class.java.simpleName
    }
}
