package com.example.codingchallenge.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.coadingchallenge.data.paging.PodPagingSource
import com.example.coadingchallenge.presentation.ui.event.PagingEvent
import com.example.coadingchallenge.domain.model.Pod
import com.example.codingchallenge.network.data.repository.PodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val _highlightedPodcast = MutableStateFlow<Pod?>(null)
    val highlightedPodcast: StateFlow<Pod?> = _highlightedPodcast.asStateFlow()

    private val favoritePodcastIds: Flow<Set<String>> =
        podsRepository.getAllFavoritePodCasts().map {
            it.toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    //Paging Setup
    private val podsPaging = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 10,
        ),
        pagingSourceFactory = {
            PodPagingSource(podsRepository)
        }
    ).flow.cachedIn(viewModelScope)

    val podCasts: Flow<PagingData<Pod>> = favoritePodcastIds.flatMapLatest { favoriteIds ->
        podsPaging.map { pagingData ->
            pagingData.map { pod ->
                pod.copy(isFavorite = favoriteIds.contains(pod.id))
            }
        }
    }.cachedIn(viewModelScope)

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
        _highlightedPodcast.value = pod
    }

    /**
     * Clear the highlighted podcast
     */
    fun clearHighlightedPodcast() {
        _highlightedPodcast.value = null
    }

    /**
     * Toggle the favorite status of a podcast
     */
    fun toggleFavoritePodcast(podcast: Pod) {
        viewModelScope.launch {
            podsRepository.toggleFavoritePodcast(podcast.id, podcast.isFavorite)
        }
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