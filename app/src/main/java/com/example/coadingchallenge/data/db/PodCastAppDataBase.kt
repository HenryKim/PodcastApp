package com.example.coadingchallenge.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coadingchallenge.data.db.dao.PodcastDao
import com.example.coadingchallenge.data.db.entity.FavoritePodcastEntity
import com.example.coadingchallenge.data.db.entity.PodcastEntity


@Database(entities = [PodcastEntity::class, FavoritePodcastEntity::class], version = 1, exportSchema = false)
abstract class PodCastAppDataBase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao
}