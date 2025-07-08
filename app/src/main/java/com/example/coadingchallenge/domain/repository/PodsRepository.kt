package com.example.codingchallenge.network.data.repository

import com.example.coadingchallenge.data.api.ApiResult
import com.example.coadingchallenge.data.db.dao.PodcastDao
import com.example.coadingchallenge.data.datasource.PodsLocalDataSource
import com.example.coadingchallenge.domain.model.Pod
import com.example.coadingchallenge.domain.utils.toPod
import com.example.coadingchallenge.domain.utils.toPodcastEntity
import com.example.codingchallenge.network.data.datasource.PodsRemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Fetches a list of pods for a given page.
 * @param page The page number to fetch. Can be null for the first page.
 * @return An [ApiResult] containing a list of [Pod] objects on success, or an error.
 */
interface PodsRepository {
    suspend fun fetchPods(page: Int?): ApiResult<List<Pod>>
    suspend fun toggleFavoritePodcast(podcastId: String, isFavorite: Boolean)
    fun getAllFavoritePodCasts(): Flow<List<String>>
}

/**
 * Implementation of [PodsRepository] that fetches pods from a remote data source.
 */
@ActivityRetainedScoped // Indicates that this class is scoped to the activity lifecycle.
class PodRepositoryImpl @Inject constructor(
    private val remoteSource: PodsRemoteDataSource,
    private val localSource: PodsLocalDataSource,
    private val podcastDao: PodcastDao
) : PodsRepository {
    override suspend fun fetchPods(page: Int?): ApiResult<List<Pod>> =
        withContext(Dispatchers.IO) {
            val cachedData: List<Pod> = podcastDao.getAllPodcasts().map { it.toPod() }
            when (val remoteResult = remoteSource.fetchPods(page)) {
                is ApiResult.Success -> {
                    if (!remoteResult.data.data.isNullOrEmpty()) {
                        podcastDao.clearAllPodcast() // FIXME This will cause data loss, if the page is not 1
                        podcastDao.addPodcasts(
                            remoteResult.data.data.map {
                                it.toPodcastEntity()
                            }
                        )
                        ApiResult.Success(
                            // If data is not available, show an empty list.
                            // This should be handled at the view level to display an appropriate message.
                            remoteResult.data.data, remoteResult.meta
                        )
                    } else {
                        ApiResult.Success(
                            // If data is not available, show cached data
                            cachedData, remoteResult.meta
                        )
                    }
                }

                is ApiResult.Error -> {
                    //checks cached data first
                    if (cachedData.isEmpty()) {
                        remoteResult
                    } else {
                        ApiResult.Success(
                            cachedData
                        )
                    }
                }
            }
        }

    override suspend fun toggleFavoritePodcast(podcastId: String, isFavorite: Boolean) {
        if (isFavorite) {
            localSource.deleteFavouritePodcast(podcastId)
        } else {
            localSource.addFavouritePodcast(podcastId)
        }
    }

    override fun getAllFavoritePodCasts(): Flow<List<String>> =
        localSource.getAllFavoritePodcastIds()
}