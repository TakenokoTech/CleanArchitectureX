package tech.takenoko.cleanarchitecturex.repository.local

import androidx.lifecycle.LiveData
import androidx.room.* // ktlint-disable no-wildcard-imports

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name: String?
)

@Dao
interface UserDao {
    @Insert suspend fun insertAll(vararg users: User)
    @Delete suspend fun delete(user: User)
    @Query(QUERY_GET_ALL) suspend fun getAll(): List<User>
    @Query(QUERY_FIND_BY_NAME) suspend fun findByName(name: String): User
    @Query(QUERY_DELETE_ALL) suspend fun deleteAll()
    @Query(QUERY_GET_ALL) fun getAllToLive(): LiveData<List<User>>
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
