package com.example.coadingchallenge.network.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coadingchallenge.network.data.db.dao.FavouritePodCastDao
import com.example.coadingchallenge.network.data.db.entity.FavoritePodCastEntity


@Database(entities = [FavoritePodCastEntity::class], version = 1, exportSchema = false)
abstract class PodCastAppDataBase : RoomDatabase() {

    abstract fun favouritePodCastDao(): FavouritePodCastDao
}