package com.example.coadingchallenge.data.api

import com.example.coadingchallenge.domain.model.PodResult
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("best_podcasts")
    suspend fun fetchPods(
        @Query("page") page: Int?
    ): Response<PodResult>
}