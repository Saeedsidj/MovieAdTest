package com.saeedev.movieadinterviewtest.di

import com.saeedev.movieadinterviewtest.data.local.dataSource.LocalDataSource
import com.saeedev.movieadinterviewtest.data.local.dataSource.LocalDataSourceImpl
import com.saeedev.movieadinterviewtest.data.network.dataSource.NetworkDataSource
import com.saeedev.movieadinterviewtest.data.network.dataSource.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindNetworkDataSource(
        impl: NetworkDataSourceImpl
    ): NetworkDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource
}
