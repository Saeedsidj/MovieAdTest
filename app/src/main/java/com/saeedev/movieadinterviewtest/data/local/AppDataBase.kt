package com.saeedev.movieadinterviewtest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.saeedev.movieadinterviewtest.data.local.TypeConverters as CustomConverter
import com.saeedev.movieadinterviewtest.data.local.dao.AdvertiseDao
import com.saeedev.movieadinterviewtest.data.local.dao.UserDao
import com.saeedev.movieadinterviewtest.data.local.dao.VideoDao
import com.saeedev.movieadinterviewtest.data.local.entity.AdvertiseEntity
import com.saeedev.movieadinterviewtest.data.local.entity.UserEntity
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity

@TypeConverters(CustomConverter::class)
@Database(
    entities = [
        VideoEntity::class,
        AdvertiseEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun advertiseDao(): AdvertiseDao
    abstract fun userDao(): UserDao
}