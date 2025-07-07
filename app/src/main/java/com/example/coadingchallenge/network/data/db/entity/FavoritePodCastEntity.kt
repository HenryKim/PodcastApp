package com.example.coadingchallenge.network.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_podcasts")
data class FavoritePodCastEntity(
    @PrimaryKey val podCastId: String,
)