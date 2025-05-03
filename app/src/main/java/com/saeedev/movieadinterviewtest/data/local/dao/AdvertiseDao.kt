package com.saeedev.movieadinterviewtest.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saeedev.movieadinterviewtest.data.local.entity.AdvertiseEntity
import com.saeedev.movieadinterviewtest.data.local.entity.VideoEntity

@Dao
interface AdvertiseDao {
    @Query("SELECT * FROM advertise")
    suspend fun getAllAdvertises(): List<AdvertiseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(advertises: List<AdvertiseEntity>)

}