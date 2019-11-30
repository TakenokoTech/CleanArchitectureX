package tech.takenoko.cleanarchitecturex.entities.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import tech.takenoko.cleanarchitecturex.repository.local.UserLocalDataSource

@Dao
interface UserDao {

    @Insert
    suspend fun insertAll(vararg users: UserLocalDataSource.User)

    @Delete
    suspend fun delete(user: UserLocalDataSource.User)

    @Query(QUERY_GET_ALL)
    suspend fun getAll(): List<UserLocalDataSource.User>

    @Query(QUERY_FIND_BY_NAME)
    suspend fun findByName(name: String): UserLocalDataSource.User

    @Query(QUERY_DELETE_ALL)
    suspend fun deleteAll()

    @Query(QUERY_GET_ALL)
    fun getAllToLive(): LiveData<List<UserLocalDataSource.User>>
}

/** ユーザー全件取得 */
const val QUERY_GET_ALL = """
    SELECT *
    FROM user
"""

/** ユーザー名から検索 */
const val QUERY_FIND_BY_NAME = """
    SELECT *
    FROM user
    WHERE name LIKE :name
    LIMIT 1
"""

/** ユーザー全件削除 */
const val QUERY_DELETE_ALL = """
    DELETE FROM user
"""
