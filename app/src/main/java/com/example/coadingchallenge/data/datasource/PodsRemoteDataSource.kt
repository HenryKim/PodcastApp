package com.example.codingchallenge.network.data.datasource


import com.example.coadingchallenge.data.api.ApiResult
import com.example.coadingchallenge.data.api.ApiService
import com.example.coadingchallenge.data.datasource.PodsDataSource
import com.example.coadingchallenge.domain.model.PodsContainer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import javax.inject.Inject

// Marks this class to be scoped to an activity and retained across configuration changes.
@ActivityRetainedScoped
class PodsRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit
) : PodsDataSource {

    // Fetches a list of podcasts from the remote API
    override suspend fun fetchPods(page: Int?): ApiResult<PodsContainer> {
        return try {
            val apiService = retrofit.create(ApiService::class.java)
            val response = apiService.fetchPods(page = page)
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
            // Catch any exceptions during the network call and return a generic error.
            ApiResult.Error(-1, "Something went wrong: ${e.message}") // Generic error for unexpected issues
        }
    }
}