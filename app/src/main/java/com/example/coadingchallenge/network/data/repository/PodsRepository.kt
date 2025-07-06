package com.example.coadingchallenge.network.data.repository

import com.example.coadingchallenge.network.data.api.ApiResult
import com.example.coadingchallenge.network.data.datasource.PodsRemoteDataSource
import com.example.coadingchallenge.network.model.Pod
import com.example.coadingchallenge.network.model.PodResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Interface for fetching pods.
 */
interface PodsRepository {
    /**
     * Fetches a list of pods for a given page.
     * @param page The page number to fetch. Can be null for the first page.
     * @return An [ApiResult] containing a list of [Pod] objects on success, or an error.
     */
    suspend fun fetchPods(page: Int?): ApiResult<List<Pod>>
}

/**
 * Implementation of [PodsRepository] that fetches pods from a remote data source.
 */
@ActivityRetainedScoped
class PodRepositoryImpl @Inject constructor(
    private val remoteSource: PodsRemoteDataSource // The remote data source for pods.
) : PodsRepository {
    override suspend fun fetchPods(page: Int?): ApiResult<List<Pod>> =
        withContext(Dispatchers.IO) {
            when (val remoteResult = remoteSource.fetchPods(page)) {
                is ApiResult.Success -> {
                    ApiResult.Success(
                        // If data is not available, show an empty list.
                        // This should be handled at the view level to display an appropriate message.
                        remoteResult.data.data ?: emptyList(),
                        remoteResult.meta)
                }

                is ApiResult.Error -> {
                    remoteResult
                }
            }
        }
}