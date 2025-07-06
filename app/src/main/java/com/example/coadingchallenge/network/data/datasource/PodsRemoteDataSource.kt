package com.example.coadingchallenge.network.data.datasource

import com.example.coadingchallenge.network.data.api.ApiResult
import com.example.coadingchallenge.network.data.api.ApiService
import com.example.coadingchallenge.network.model.PodsContainer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import javax.inject.Inject

// Marks this class to be scoped to an activity and retained across configuration changes.
@ActivityRetainedScoped
// Remote data source for fetching podcasts.
// Injects Retrofit to make network requests.
class PodsRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit
) : PodsDataSource {

    // Fetches a list of podcasts from the remote API.
    // page: The page number to fetch.
    // Returns an ApiResult which can be either Success with PodsContainer or Error.
    override suspend fun fetchPods(page: Int?): ApiResult<PodsContainer> {
        return try {
            // Create an instance of the ApiService using Retrofit.
            val apiService = retrofit.create(ApiService::class.java)
            // Make the network call to fetch podcasts.
            val response = apiService.fetchPods(page = page)
            val body = response.body()

            // Check if the response was successful and the body is not null.
            if (response.isSuccessful && body != null) {
                // Create a PodsContainer from the response data.
                val result = PodsContainer(
                    data = body.podcasts,
                    url = response.raw().request.url.toString() // Get the request URL.
                )
                // Return a successful result.
                ApiResult.Success(result)
            } else {
                // Return an error result with the response code and message.
                ApiResult.Error(
                    code = response.code(),
                    message = response.message()
                )
            }
        } catch (e: Exception) {
            // Catch any exceptions during the network call and return a generic error.
            ApiResult.Error(-1, "Something went wrong: ${e.message}")
        }
    }
}