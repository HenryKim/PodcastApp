package com.example.coadingchallenge.network.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coadingchallenge.network.data.db.entity.FavoritePodCastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  FavouritePodCastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favouritePodCast: FavoritePodCastEntity)

    @Delete
    suspend fun deleteFavorite(entity: FavoritePodCastEntity)

    @Query("SELECT podcastId FROM favorite_podcasts")
    fun getAllFavoritePodcastIds(): Flow<List<String>>
}