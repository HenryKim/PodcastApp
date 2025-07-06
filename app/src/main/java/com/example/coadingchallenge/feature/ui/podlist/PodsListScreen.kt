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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodsListScreen (
    modifier: Modifier,
    podsListViewModel: PodCastViewModel = hiltViewModel(),
    onPodCastClick: () -> Unit
) {
    val podListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    //collect the paging data flow
    val podsPaging = podsListViewModel.podsPaging.collectAsLazyPagingItems()

    //collect isRefreshing state
    val isRefreshing by podsListViewModel.isRefreshing.collectAsState()

    LaunchedEffect(podsPaging.loadState.refresh) {
        var refresh = podsPaging.loadState.refresh

        // show snackbar error when load state is error that comes
        // from pull to refresh
        if (refresh is LoadState.Error && isRefreshing) {
            snackbarHostState.showSnackbar(
                message =  refresh.error.message.takeIf { !it.isNullOrBlank() } ?: run {
                    context.getString(R.string.unable_to_load_pods)
                }
            )
        }

        // reset the pull to refresh state
        if (refresh is LoadState.NotLoading && isRefreshing) {
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
            isRefreshing = isRefreshing &&
            podsPaging.loadState.refresh is LoadState.Loading,
            onRefresh = { podsListViewModel.onPullToRefresh() },
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.animateContentSize(),
                contentPadding = PaddingValues(16.dp),
                state = podListState
            ) {
                val refresh = podsPaging.loadState.refresh
                if(refresh is LoadState.Loading && !isRefreshing) {
                    item(key = "loading") {
                        Loading(modifier = Modifier.fillParentMaxSize())
                    }
                } else if (refresh is LoadState.Error && !isRefreshing) {
                    item(key = "error") {
                        ErrorState(
                            modifier = Modifier.fillParentMaxSize(),
                            message = refresh.error.message,
                            onRetry = {
                                // trigger paging event retry
                                podsListViewModel.onRetryFetch()
                            }
                        )
                    }
                } else {
                    items(
                        count = podsPaging.itemCount
                    ) { index ->
                        podsPaging[index]?.let { pods ->
                            key(pods.id.hashCode()) {
                                PodsCard(
                                    podcast = pods,
                                    onItemClick = { onPodCastClick() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

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
            modifier = Modifier.padding(bottom = 4.dp),
            text = message.takeIf { !it.isNullOrBlank() } ?: run {
                stringResource(R.string.unable_to_load_pods)
            }
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}