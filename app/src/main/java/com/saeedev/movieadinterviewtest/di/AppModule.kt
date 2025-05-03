package com.saeedev.movieadinterviewtest.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.saeedev.movieadinterviewtest.common.Constants
import com.saeedev.movieadinterviewtest.data.local.AppDataBase
import com.saeedev.movieadinterviewtest.data.local.dataSource.LocalDataSource
import com.saeedev.movieadinterviewtest.data.network.VideoAdApi
import com.saeedev.movieadinterviewtest.data.network.dataSource.NetworkDataSource
import com.saeedev.movieadinterviewtest.data.repository.VideoAdRepositoryImpl
import com.saeedev.movieadinterviewtest.domain.repository.VideoAdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdManager(): AdManager {
        return AdManager()
    }

    @Provides
    @Singleton
    fun provideMovieAdApi(): VideoAdApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VideoAdApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVideosAdRepository(api: NetworkDataSource, db: LocalDataSource): VideoAdRepository {
        return VideoAdRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "movieAd")
            .addCallback(callback).build()
    }

    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                db.execSQL(
                    """
                    INSERT INTO user (id, name, loggedIn) VALUES
                    (1, 'alex',1),
                     (2, 'flux',0);
                """.trimIndent()
                )
            }
        }
    }

}