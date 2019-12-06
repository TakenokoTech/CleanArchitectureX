package tech.takenoko.cleanarchitecturex.di

import tech.takenoko.cleanarchitecturex.entities.room.UserDao

class MockDatabase : AppDatabase {

    override fun userDao(): UserDao = userDao

    companion object {
        lateinit var userDao: UserDao
    }
}
