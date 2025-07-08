package com.example.coadingchallenge.di

import com.example.coadingchallenge.data.datasource.PodsLocalDataSource
import com.example.coadingchallenge.data.db.dao.PodcastDao
import com.example.codingchallenge.network.data.datasource.PodsRemoteDataSource
import com.example.codingchallenge.network.data.repository.PodRepositoryImpl
import com.example.codingchallenge.network.data.repository.PodsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object PodsModule {
    @Provides
    @ActivityRetainedScoped
    fun providePodsRepository(
        remoteSource: PodsRemoteDataSource,
        localSource: PodsLocalDataSource,
        podcastDao: PodcastDao
    ): PodsRepository = PodRepositoryImpl(remoteSource, localSource, podcastDao)

    @Provides
    @ActivityRetainedScoped
    fun providePodsRemoteDataSource(
        retrofit: Retrofit
    ): PodsRemoteDataSource = PodsRemoteDataSource(retrofit)

    @Provides
    @ActivityRetainedScoped
    fun providePodsLocalDataSource(
        podcastDao: PodcastDao
    ): PodsLocalDataSource = PodsLocalDataSource(podcastDao)

}