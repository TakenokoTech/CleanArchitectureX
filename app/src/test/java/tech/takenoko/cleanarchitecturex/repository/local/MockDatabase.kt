package tech.takenoko.cleanarchitecturex.repository.local

import tech.takenoko.cleanarchitecturex.di.AppDatabase
import tech.takenoko.cleanarchitecturex.entities.room.UserDao

class MockDatabase : AppDatabase {

    override fun userDao(): UserDao = userDao

    companion object {
        lateinit var userDao: UserDao
    }
}
