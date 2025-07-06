package com.example.coadingchallenge.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.coadingchallenge.network.data.paging.PodPagingSource
import com.example.coadingchallenge.network.data.repository.PodsRepository
import com.example.coadingchallenge.network.model.Pod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodCastViewModel @Inject constructor(
    private val podsRepository: PodsRepository
):ViewModel(){

    // the pull to refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    // the paging event
    private val _pagingEvent = MutableSharedFlow<PagingEvent>()
    val pagingEvent: SharedFlow<PagingEvent> = _pagingEvent

    // user selected podcast to highlight
    private val _highligtedPodcast = MutableStateFlow<Pod?>(null)
    val highlightedPodcast: StateFlow<Pod?> = _highligtedPodcast.asStateFlow()

    //Paging Setup
    val podsPaging = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 10,
        ),
        pagingSourceFactory = {
            PodPagingSource(podsRepository)
        }
    ).flow.cachedIn(viewModelScope)

    init {
        // on launch just trigger pagers to reload the data
        viewModelScope.launch {
            _pagingEvent.emit(PagingEvent.RELOAD)
        }
    }

    /**
     * Highlight the selected podcast
     */
    fun onPodCastClick(pod: Pod) {
        _highligtedPodcast.value = pod
    }

    /**
     * Clear the highlighted podcast
     */
    fun clearHighlightedPodcast() {
        _highligtedPodcast.value = null
    }



    /**
     * Update the [isRefreshing] ui state
     */
    fun setRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    /**
     * Trigger [PagingEvent.RETRY]
     */
    fun onRetryFetch() = viewModelScope.launch {
        _pagingEvent.emit(PagingEvent.RETRY)
    }

    /**
     * Trigger [PagingEvent.REFRESH]
     */
    fun onPullToRefresh() = viewModelScope.launch {
        _pagingEvent.emit(PagingEvent.REFRESH)
    }


}