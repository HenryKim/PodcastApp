package com.example.coadingchallenge.feature.ui.podlist

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
import com.example.coadingchallenge.feature.PagingEvent
import com.example.coadingchallenge.feature.PodCastViewModel
import com.example.coadingchallenge.network.model.Pod


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
    // Access the current context, useful for things like string resources.
    val context = LocalContext.current

    // Collects the Flow of PagingData from the ViewModel and converts it into LazyPagingItems
    // which can be easily used with LazyColumn.
    val podsPaging = podsListViewModel.podsPaging.collectAsLazyPagingItems()

    // Collects the isRefreshing state from the ViewModel to control the PullToRefreshBox.
    val isRefreshing by podsListViewModel.isRefreshing.collectAsState()

    // LaunchedEffect to observe changes in the Paging refresh load state.
    // It triggers side effects like showing a Snackbar on error or resetting refresh state.
    LaunchedEffect(podsPaging.loadState.refresh) {
        val refreshLoadState = podsPaging.loadState.refresh

        // Show a Snackbar if there's an error during pull-to-refresh.
        if (refreshLoadState is LoadState.Error && isRefreshing) {
            snackbarHostState.showSnackbar(
                message = refreshLoadState.error.message.takeIf { !it.isNullOrBlank() }
                    ?: context.getString(R.string.unable_to_load_pods) // Fallback error message
            )
        }

        // Reset the isRefreshing state in the ViewModel once loading (after pull-to-refresh) is complete.
        if (refreshLoadState is LoadState.NotLoading && isRefreshing) {
            podsListViewModel.setRefreshing(false)
        }
    }

    // LaunchedEffect to collect and handle PagingEvents from the ViewModel.
    // This allows the ViewModel to trigger actions like refresh or retry on the Paging data.
    // `Unit` as a key means this effect runs once when the composable enters the composition.
    LaunchedEffect(Unit) {
        podsListViewModel.pagingEvent.collect { event ->
            when (event) {
                PagingEvent.REFRESH -> {
                    podsListViewModel.setRefreshing(true) // Indicate that a refresh is in progress
                    podsPaging.refresh() // Trigger a refresh of the PagingData
                }
                PagingEvent.RETRY -> podsPaging.retry() // Retry the last failed Paging load
                PagingEvent.RELOAD -> podsPaging.refresh() // Equivalent to REFRESH for now
            }
        }
    }

    // Scaffold provides the basic Material Design visual layout structure.
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            // SnackbarHost composable to display Snackbars using the snackbarHostState.
            androidx.compose.material3.SnackbarHost(snackbarHostState)
        },
        topBar = {
            // Surface provides a background color and elevation for the TopAppBar.
            Surface(
                modifier = Modifier.fillMaxWidth(),
                // shadowElevation = 4.dp // Optional: add shadow to the TopAppBar
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily.Serif, // Example of custom font family
                            fontWeight = FontWeight.Black // Example of custom font weight
                        )
                    }
                )
            }
        },
    ) { innerPadding -> // Padding values provided by Scaffold to avoid overlapping with system UI.
        // PullToRefreshBox enables the swipe-down-to-refresh user interaction.
        PullToRefreshBox(
            isRefreshing = isRefreshing && podsPaging.loadState.refresh is LoadState.Loading,
            onRefresh = { podsListViewModel.onPullToRefresh() }, // Action to perform on refresh
            modifier = Modifier.padding(innerPadding) // Apply inner padding from Scaffold
        ) {
            // LazyColumn efficiently displays a scrollable list of items.
            LazyColumn(
                modifier = Modifier.animateContentSize(), // Animates size changes in the list.
                contentPadding = PaddingValues(16.dp), // Padding around the content of the list.
                state = podListState // Attach the LazyListState.
            ) {
                // Determine the current state of the initial page load (refresh).
                val refreshLoadState = podsPaging.loadState.refresh

                // Display a full-screen loading indicator if the initial load is in progress
                // and it's not a pull-to-refresh action.
                if (refreshLoadState is LoadState.Loading && !isRefreshing) {
                    item(key = "loading_indicator_full") { // Unique key for this item
                        Loading(modifier = Modifier.fillParentMaxSize()) // Custom Loading composable
                    }
                }
                // Display a full-screen error state if the initial load failed
                // and it's not a pull-to-refresh action.
                else if (refreshLoadState is LoadState.Error && !isRefreshing) {
                    item(key = "error_state_full") { // Unique key for this item
                        ErrorState(
                            modifier = Modifier.fillParentMaxSize(), // Custom ErrorState composable
                            message = refreshLoadState.error.message,
                            onRetry = {
                                podsListViewModel.onRetryFetch() // Trigger a retry via ViewModel
                            }
                        )
                    }
                }
                // If not loading or error during initial load (or if data is already present), display podcast items.
                else {
                    // `items` block for displaying the paged data.
                    items(
                        count = podsPaging.itemCount,
                        // key = { index -> podsPaging.peek(index)?.id ?: index } // Alternative key provider
                    ) { index ->
                        // Get the podcast item at the current index.
                        podsPaging[index]?.let { podcast ->
                            // `key` composable provides a stable and unique key for each item,
                            // which helps Compose optimize recompositions and maintain scroll position.
                            key(podcast.id.hashCode()) {
                                PodsCard( // Custom composable to display a single podcast item.
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
                                    onRetry = { podsPaging.retry() } // Retry appending items
                                )
                            }
                        }
                        is LoadState.NotLoading -> {
                            if (podsPaging.loadState.append.endOfPaginationReached && podsPaging.itemCount == 0 && refreshLoadState !is LoadState.Loading) {
                                // Optional: Show an "empty list" message if pagination ended,
                                // no items were loaded, and it's not currently loading.
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
        modifier = modifier.padding(24.dp), // Padding around the indicator
        contentAlignment = Alignment.Center // Centers the indicator within the Box
    ) {
        CircularProgressIndicator() // Standard Material Design loading indicator
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
        modifier = modifier.padding(16.dp), // Padding around the error content
        horizontalAlignment = Alignment.CenterHorizontally, // Centers children horizontally
        verticalArrangement = Arrangement.Center // Centers children vertically
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp), // Space below the error message
            text = message.takeIf { !it.isNullOrBlank() }
                ?: stringResource(R.string.unable_to_load_pods) // Default error message
        )
        Button(onClick = onRetry) { // Retry button
            Text(text = stringResource(R.string.retry))
        }
    }
}
