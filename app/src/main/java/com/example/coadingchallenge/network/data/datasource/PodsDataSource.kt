package com.example.coadingchallenge.network.data.datasource

import com.example.coadingchallenge.network.data.api.ApiResult
import com.example.coadingchallenge.network.model.PodsContainer

interface PodsDataSource {
    suspend fun fetchPods(page: Int?): ApiResult<PodsContainer>
}