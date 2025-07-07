package com.example.coadingchallenge.data.datasource

import com.example.coadingchallenge.data.api.ApiResult
import com.example.coadingchallenge.data.db.dao.PodcastDao
import com.example.coadingchallenge.data.db.entity.FavoritePodcastEntity
import com.example.coadingchallenge.domain.model.PodsContainer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class PodsLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
): PodsDataSource {
    override suspend fun fetchPods(page: Int?): ApiResult<PodsContainer> {
        TODO("Not yet implemented")
    }


    suspend fun addFavouritePodcast(podcastId: String) {
        podcastDao.insertFavorite(
            FavoritePodcastEntity(podCastId = podcastId)
        )
    }

    suspend fun deleteFavouritePodcast(podcastId: String) {
        podcastDao.deleteFavorite(podcastId = podcastId)
    }

    fun getAllFavoritePodcastIds(): Flow<List<String>> {
        return podcastDao.getAllFavoritePodcastIds()
    }

}