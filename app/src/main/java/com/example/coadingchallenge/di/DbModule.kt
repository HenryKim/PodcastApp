package com.example.coadingchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.coadingchallenge.network.data.db.PodCastAppDataBase
import com.example.coadingchallenge.network.data.db.dao.FavouritePodCastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PodCastAppDataBase {
        return Room.databaseBuilder(
            context= context,
            klass = PodCastAppDataBase::class.java,
            name = "podcast_database",
        ).build()
    }

    @Singleton
    @Provides
    fun providePodcastDao(database: PodCastAppDataBase): FavouritePodCastDao {
        return database.favouritePodCastDao()
    }

}