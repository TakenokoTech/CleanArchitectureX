package tech.takenoko.cleanarchitecturex.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.takenoko.cleanarchitecturex.entities.room.UserDao
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource

@Database(entities = [UserLocalDataSource.User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase = synchronized(this) {
            instance = if (instance == null) Room.databaseBuilder(context, AppDatabase::class.java, "CleanArchitectureX-DB").build() else instance
            return instance!!
        }
    }
}
