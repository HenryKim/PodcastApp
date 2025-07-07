package com.example.coadingchallenge.data.datasource

import com.example.coadingchallenge.data.api.ApiResult
import com.example.coadingchallenge.domain.model.PodsContainer

interface PodsDataSource {
    suspend fun fetchPods(page: Int?): ApiResult<PodsContainer>
}