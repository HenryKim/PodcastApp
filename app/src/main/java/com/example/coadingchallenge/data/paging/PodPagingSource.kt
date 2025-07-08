package com.example.coadingchallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coadingchallenge.data.api.ApiResult
import com.example.coadingchallenge.domain.model.Pod
import com.example.codingchallenge.network.data.repository.PodsRepository


class PodPagingSource (
    private val podsRepository: PodsRepository
): PagingSource<Int, Pod>() {
    override fun getRefreshKey(state: PagingState<Int, Pod>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // anchorPosition is the most recent accessed item by index
            // (eg. the current visible item on the screen)
            // try to find the anchor page info based on the visible item (anchorPosition)
            val anchorPage = state.closestPageToPosition(anchorPosition)
            // get the current page from the anchor page info
            // anchor page only has prev and next page info
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pod> {
        val result = podsRepository.fetchPods( page = params.key)
        val page = params.key ?: 1
        return when (result) {
            is ApiResult.Success -> LoadResult.Page(
                data = result.data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.data.isEmpty()) null else page + 1
            )
            is  ApiResult.Error -> LoadResult.Error(Exception(result.message))
        }
    }

}