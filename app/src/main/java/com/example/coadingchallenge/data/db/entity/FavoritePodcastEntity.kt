package com.example.coadingchallenge.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_podcasts")
data class FavoritePodcastEntity(
    @PrimaryKey val podCastId: String,
)