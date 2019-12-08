package tech.takenoko.cleanarchitecturex.repository.local

import androidx.annotation.MainThread
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
        val result = database.userDao().insertAll(*users)
        AppLog.debug(TAG, "insertAll. ${users.map { it.id }}")
        return result
    }

    @WorkerThread
    override suspend fun deleteAll() {
        val result = database.userDao().deleteAll()
        AppLog.debug(TAG, "deleteAll.")
        return result
    }

    @MainThread
    override fun getAllToLive(): LiveData<List<User>> {
        return database.userDao().getAllToLive()
    }

    @Entity
    data class User(
        @ColumnInfo(name = "userName") val userName: String,
        @ColumnInfo(name = "displayName") val displayName: String,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
    )

    companion object {
        private val TAG = UserLocalDataSource::class.java.simpleName
    }
}
