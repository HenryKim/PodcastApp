package com.example.coadingchallenge.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast")
data class PodcastEntity(
    @PrimaryKey
    val podCastId: String,
    val title: String,
    val publisher: String,
    val thumbnailUrl: String,
    val description: String,
    val imageUrl: String
)