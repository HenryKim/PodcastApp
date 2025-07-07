package com.example.coadingchallenge.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.coadingchallenge.data.db.entity.FavoritePodcastEntity
import com.example.coadingchallenge.data.db.entity.PodcastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  PodcastDao {
    @Upsert
    suspend fun addPodcasts(podcast: List<PodcastEntity>)

    @Query("DELETE FROM podcast")
    suspend fun clearAllPodcast()

    @Query("SELECT * FROM podcast")
    fun getAllPodcasts(): List<PodcastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favouritePodCast: FavoritePodcastEntity)

    @Query("DELETE FROM favorite_podcasts WHERE podcastId = :podcastId")
    suspend fun deleteFavorite(podcastId: String)

    @Query("SELECT podcastId FROM favorite_podcasts")
    fun getAllFavoritePodcastIds(): Flow<List<String>>
}