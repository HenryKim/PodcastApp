package com.example.coadingchallenge.di

import com.example.coadingchallenge.network.data.datasource.PodsRemoteDataSource
import com.example.coadingchallenge.network.data.repository.PodRepositoryImpl
import com.example.coadingchallenge.network.data.repository.PodsRepository
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
        remoteSource: PodsRemoteDataSource
    ): PodsRepository = PodRepositoryImpl(remoteSource)

    @Provides
    @ActivityRetainedScoped
    fun providePodsRemoteDataSource(
        retrofit: Retrofit
    ): PodsRemoteDataSource = PodsRemoteDataSource(retrofit)

}