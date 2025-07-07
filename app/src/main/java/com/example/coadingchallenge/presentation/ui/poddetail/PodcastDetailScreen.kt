package com.example.coadingchallenge.presentation.ui.poddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.coadingchallenge.R
import com.example.coadingchallenge.domain.model.Pod

/**
 * A preview composable for the [PodcastDetailScreen].
 * It creates a sample [Pod] object to display the screen's layout.
 */
@Preview(showBackground = true)
@Composable
fun PodcastDetailScreenPreview() {
    val pod = Pod(
        id = "1",
        rss = "rss",
        type = "type",
        email = "email@example.com",
        extra = null,
        image = "https://example.com/image.jpg",
        title = "The Art of Doing Nothing and Accomplishing Everything",
        country = "US",
        website = "https://example.com",
        language = "English",
        genreIds = listOf(1, 2, 3),
        itunesId = 1234567890,
        publisher = "The Minimalist Publishers",
        thumbnail = "https://example.com/thumbnail.jpg",
        isClaimed = true,
        description = "This is a detailed description of the podcast, exploring various topics and themes. It aims to provide listeners with insightful content and engaging discussions. ".repeat(5),
        lookingFor = null,
        hasSponsors = true,
        listenScore = 100,
        totalEpisodes = 10,
        listenNotesUrl = "https://listennotes.com/example",
        audioLengthSec = 3600,
        explicitContent = true,
        latestEpisodeId = "latestEpisodeId",
        latestPubDateMs = 1678886400000L,
        earliestPubDateMs = 1678886400000L,
        hasGuestInterviews = true,
        updateFrequencyHours = 24,
        listenScoreGlobalRank = "123"
    )
    PodcastDetailScreen(
        podcast = pod,
        onBack = { /* No-op for preview */ },
        onFavoriteClick = { /* No-op for preview */ }
    )
}

/**
 * Composable function that displays the detailed view of a podcast.
 *
 * @param podcast The [Pod] object containing details to display. Can be null if data is loading or not available.
 * @param onBack Callback function to be invoked when the back navigation is triggered.
 * @param onFavoriteClick Callback function to be invoked when the favorite button is clicked.
 *                        It receives the non-null [Pod] object.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailScreen(
    podcast: Pod?,
    onBack: () -> Unit = {},
    onFavoriteClick: (podcast: Pod) -> Unit = {}
) {
    // Retrieves the current screen width in Dp.
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // State to track the current favorite status of the podcast.
    var isFavorite by remember(podcast?.isFavorite) {
        mutableStateOf(podcast?.isFavorite ?: false)
    }

    // Remembers the state for the SnackbarHost, used for showing temporary messages.
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.back),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            podcast?.let { currentPodcast ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = currentPodcast.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = currentPodcast.publisher,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AsyncImage(
                        model = currentPodcast.thumbnail,
                        contentDescription = stringResource(R.string.contentDescription_podcastThumnails),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(screenWidth * 0.6f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Button(
                        onClick = {
                            onFavoriteClick(currentPodcast)
                            isFavorite = !isFavorite
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFavorite) Color.Magenta else MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.8f),

                    ) {
                        Text(
                            text = stringResource(if (isFavorite) R.string.favorited else R.string.favorite),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = currentPodcast.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
