package tech.takenoko.cleanarchitecturex.repository.dao

import androidx.room.*

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name: String?
)

@Dao
interface UserDao {

    @Query("""
        SELECT *
        FROM user
        """)
    fun getAll(): List<User>

    @Query("""
        SELECT * 
        FROM user 
        WHERE uid IN (:userIds)
        """)
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("""
        SELECT *
        FROM user
        WHERE name LIKE :name
        LIMIT 1
        """)
    fun findByName(name: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
