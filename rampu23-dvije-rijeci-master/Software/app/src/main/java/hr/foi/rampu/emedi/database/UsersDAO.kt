package hr.foi.rampu.emedi.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import hr.foi.rampu.emedi.entities.User

@Dao
interface UsersDAO {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Int): User

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByCredentials(username: String, password: String): User

    @Insert(onConflict = REPLACE)
    fun insertUser(vararg user: User): List<Long>

    @Delete
    fun deleteUser(vararg user: User)
}