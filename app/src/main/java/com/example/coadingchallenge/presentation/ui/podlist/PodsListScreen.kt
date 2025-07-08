package com.example.coadingchallenge.presentation.ui.podlist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coadingchallenge.R
import com.example.coadingchallenge.presentation.ui.event.PagingEvent
import com.example.coadingchallenge.domain.model.Pod
import com.example.codingchallenge.feature.PodCastViewModel


/**
 * Composable function that displays a list of podcasts with pull-to-refresh and pagination.
 *
 * @param modifier Modifier for this composable.
 * @param podsListViewModel The ViewModel responsible for fetching and managing podcast data.
 *                          It's injected using Hilt.
 * @param onPodCastClick Callback function invoked when a podcast item is clicked.
 *                       It receives the clicked [Pod] object.
 */
@OptIn(ExperimentalMaterial3Api::class) // Opt-in for Material 3 experimental APIs like PullToRefreshBox
@Composable
fun PodsListScreen(
    modifier: Modifier = Modifier, // Default Modifier if none is provided
    podsListViewModel: PodCastViewModel = hiltViewModel(),
    onPodCastClick: (Pod) -> Unit
) {
    // State for the LazyColumn, allows controlling and observing scroll position.
    val podListState = rememberLazyListState()
    // State for the SnackbarHost, used to show temporary messages (e.g., errors).
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Collects the Flow of PagingData from the ViewModel and converts it into LazyPagingItems
    val podsPaging = podsListViewModel.podCasts.collectAsLazyPagingItems()

    // Collects the isRefreshing state from the ViewModel to control the PullToRefreshBox.
    val isRefreshing by podsListViewModel.isRefreshing.collectAsState()

    LaunchedEffect(podsPaging.loadState.refresh) {
        val refreshLoadState = podsPaging.loadState.refresh

        // Show a Snackbar if there's an error during pull-to-refresh.
        if (refreshLoadState is LoadState.Error && isRefreshing) {
            snackbarHostState.showSnackbar(
                message = refreshLoadState.error.message.takeIf { !it.isNullOrBlank() }
                    ?: context.getString(R.string.unable_to_load_pods)
            )
        }

        // Reset the isRefreshing state in the ViewModel once loading (after pull-to-refresh) is complete.
        if (refreshLoadState is LoadState.NotLoading && isRefreshing) {
            podsListViewModel.setRefreshing(false)
        }
    }

    LaunchedEffect(Unit) {
        podsListViewModel.pagingEvent.collect { event ->
            when (event) {
                PagingEvent.REFRESH -> {
                    podsListViewModel.setRefreshing(true)
                    podsPaging.refresh()
                }
                PagingEvent.RETRY -> podsPaging.retry()
                PagingEvent.RELOAD -> podsPaging.refresh()
            }
        }
    }

    // Scaffold provides the basic Material Design visual layout structure.
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(snackbarHostState)
        },
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Black
                        )
                    }
                )
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing && podsPaging.loadState.refresh is LoadState.Loading,
            onRefresh = { podsListViewModel.onPullToRefresh() },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.animateContentSize(),
                contentPadding = PaddingValues(16.dp),
                state = podListState
            ) {
                // Determine the current state of the initial page load (refresh).
                val refreshLoadState = podsPaging.loadState.refresh

                if (refreshLoadState is LoadState.Loading && !isRefreshing) {
                    item(key = "loading_indicator_full") {
                        Loading(modifier = Modifier.fillParentMaxSize())
                    }
                }
                else if (refreshLoadState is LoadState.Error && !isRefreshing) {
                    item(key = "error_state_full") {
                        ErrorState(
                            modifier = Modifier.fillParentMaxSize(),
                            message = refreshLoadState.error.message,
                            onRetry = {
                                podsListViewModel.onRetryFetch()
                            }
                        )
                    }
                }
                // If not loading or error during initial load (or if data is already present), display podcast items.
                else {
                    items(
                        count = podsPaging.itemCount,
                    ) { index ->
                        podsPaging[index]?.let { podcast ->
                            key(podcast.id.hashCode()) {
                                PodsCard(
                                    podcast = podcast,
                                    onItemClick = { onPodCastClick(podcast) }
                                )
                            }
                        }
                    }

                    // Handle loading state for appended items (pagination).
                    when (podsPaging.loadState.append) {
                        is LoadState.Loading -> {
                            item(key = "loading_indicator_append") {
                                Loading(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                            }
                        }
                        is LoadState.Error -> {
                            val error = podsPaging.loadState.append as LoadState.Error
                            item(key = "error_indicator_append") {
                                ErrorState(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    message = error.error.message,
                                    onRetry = { podsPaging.retry() }
                                )
                            }
                        }
                        is LoadState.NotLoading -> {
                            if (podsPaging.loadState.append.endOfPaginationReached && podsPaging.itemCount == 0 && refreshLoadState !is LoadState.Loading) {
                                item(key = "empty_list_message") {
                                    Text(
                                        text = stringResource(R.string.no_podcasts_found),
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A private composable function to display a loading indicator.
 *
 * @param modifier Modifier for this composable.
 */
@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * A private composable function to display an error message with a retry button.
 *
 * @param onRetry Callback function to be invoked when the retry button is clicked.
 * @param modifier Modifier for this composable.
 * @param message The error message to display. If null or blank, a default message is shown.
 */
@Composable
private fun ErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = message.takeIf { !it.isNullOrBlank() }
                ?: stringResource(R.string.unable_to_load_pods)
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}
