package com.example.coadingchallenge.network.data.repository

import com.example.coadingchallenge.network.data.api.ApiResult
import com.example.coadingchallenge.network.data.datasource.PodsRemoteDataSource
import com.example.coadingchallenge.network.model.Pod
import com.example.coadingchallenge.network.model.PodResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PodsRepository {
    suspend fun fetchPods(page: Int?): ApiResult<List<Pod>>
}

@ActivityRetainedScoped
class PodRepositoryImpl @Inject constructor(
    private val remoteSource: PodsRemoteDataSource
) : PodsRepository {
    override suspend fun fetchPods(page: Int?): ApiResult<List<Pod>> =
        withContext(Dispatchers.IO) {
            when (val remoteResult = remoteSource.fetchPods(page)) {
                is ApiResult.Success -> {
                    ApiResult.Success(
                        remoteResult.data.data ?: emptyList(),
                        remoteResult.meta)
                }

                is ApiResult.Error -> {
                    remoteResult
                }
            }
        }
}