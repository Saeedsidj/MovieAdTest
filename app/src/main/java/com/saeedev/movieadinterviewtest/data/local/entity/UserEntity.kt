package com.saeedev.movieadinterviewtest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saeedev.movieadinterviewtest.domain.model.User

@Entity("user")
data class UserEntity(
    @PrimaryKey
    val id : Int,
    val name : String,
    val loggedIn : Boolean,
){
    fun toDomain() = User(
        id = id,
        name = name,
        isLoggedIn = loggedIn
    )
}
