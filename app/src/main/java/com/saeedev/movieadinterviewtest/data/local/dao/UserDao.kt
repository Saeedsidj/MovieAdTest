package com.saeedev.movieadinterviewtest.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.saeedev.movieadinterviewtest.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id = :currentUser")
    suspend fun getUserById(currentUser : Int) : UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE user SET loggedIn = 0")
    suspend fun logoutAllUsers()

    @Query("UPDATE user SET loggedIn = 1 WHERE id = :userId")
    suspend fun loginUserById(userId: Int)
}