package com.example.coadingchallenge.network.data.datasource

import com.example.coadingchallenge.network.data.api.ApiResult
import com.example.coadingchallenge.network.data.api.ApiService
import com.example.coadingchallenge.network.model.PodResult
import com.example.coadingchallenge.network.model.PodsContainer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import javax.inject.Inject

@ActivityRetainedScoped
class PodsRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit
): PodsDataSource {

    override suspend fun fetchPods(page: Int?): ApiResult<PodsContainer> = try {
        val response = retrofit.create(ApiService::class.java)
            .fetchPods(
                page = page
            )
        val body = response.body()
        if (response.isSuccessful && body != null) {
            val result = PodsContainer(
                data = body.podcasts,
                url = response.raw().request.url.toString()
            )
            ApiResult.Success(result)
        } else {
            ApiResult.Error(
                code = response.code(),
                message = response.message()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(-1, "Something went wrong")
    }
}