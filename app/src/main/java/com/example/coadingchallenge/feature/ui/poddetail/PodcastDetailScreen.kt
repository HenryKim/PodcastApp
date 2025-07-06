package com.example.coadingchallenge.feature.ui.poddetail

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.coadingchallenge.network.model.Pod

/**
 * A preview composable for the [PodcastDetailScreen].
 * It creates a sample [Pod] object to display the screen's layout.
 */
@Preview(showBackground = true)
@Composable
fun PodcastDetailScreenPreview() {
    val pod = Pod( // Sample Pod data for previewing the UI
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
@OptIn(ExperimentalMaterial3Api::class) // Opt-in for Material 3 experimental APIs
@Composable
fun PodcastDetailScreen(
    podcast: Pod?,
    onBack: () -> Unit = {},
    onFavoriteClick: (podcast: Pod) -> Unit = {}
) {
    // Retrieves the current screen width in Dp.
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Remembers the state for the SnackbarHost, used for showing temporary messages.
    val snackBarHostState = remember { SnackbarHostState() }

    // Scaffold provides the basic Material Design visual layout structure.
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            // TopAppBar displays information and actions relating to the current screen.
            TopAppBar(
                title = {
                    Text(
                        // text = stringResource(R.string.podcast_details), // Changed to "Back" for consistency with icon
                        text = stringResource(R.string.back),
                        style = MaterialTheme.typography.titleLarge, // Using a larger title style for the app bar
                        maxLines = 1 // Ensures the title does not wrap to multiple lines
                    )
                },
                navigationIcon = {
                    // IconButton for the back navigation action.
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Standard back arrow icon
                            contentDescription = stringResource(R.string.back) // Accessibility description
                        )
                    }
                }
            )
        }
    ) { paddingValues -> // Content area padding provided by Scaffold
        // Box is a basic layout composable that positions its children relative to its edges.
        Box(
            modifier = Modifier
                .fillMaxSize() // The Box will take up all available space.
                .padding(paddingValues) // Apply padding from Scaffold to respect system UI elements.
        ) {
            // Check if podcast data is available before attempting to display it.
            podcast?.let { currentPodcast ->
                // Column arranges its children vertically.
                Column(
                    modifier = Modifier
                        .fillMaxSize() // The Column will take up all available space within the Box.
                        .verticalScroll(rememberScrollState()) // Enables vertical scrolling if content overflows.
                        .padding(horizontal = 16.dp, vertical = 16.dp), // Padding for the content area.
                    horizontalAlignment = Alignment.CenterHorizontally, // Centers children horizontally.
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Adds 16.dp of space between each child.
                ) {
                    // Displays the podcast title.
                    Text(
                        text = currentPodcast.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold // Makes the title bold.
                        ),
                        textAlign = TextAlign.Center, // Centers the text.
                        modifier = Modifier.fillMaxWidth() // Text takes the full width available.
                    )

                    // Displays the podcast publisher.
                    Text(
                        text = currentPodcast.publisher,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontStyle = FontStyle.Italic, // Italicizes the publisher name.
                            color = MaterialTheme.colorScheme.onSurfaceVariant // Uses a less prominent color.
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // AsyncImage loads and displays the podcast thumbnail from a URL.
                    AsyncImage(
                        model = currentPodcast.thumbnail, // URL of the image to load.
                        contentDescription = stringResource(R.string.contentDescription_podcastThumnails), // Accessibility.
                        contentScale = ContentScale.Crop, // Crops the image to fill its bounds.
                        modifier = Modifier
                            .width(screenWidth * 0.6f) // Sets image width to 60% of screen width.
                            .aspectRatio(1f) // Maintains a 1:1 aspect ratio (square).
                            .clip(RoundedCornerShape(16.dp)) // Clips the image to have rounded corners.
                    )

                    // Button to mark the podcast as a favorite.
                    Button(
                        onClick = { onFavoriteClick(currentPodcast) }, // Action when button is clicked.
                        shape = RoundedCornerShape(12.dp), // Custom shape for the button.
                        modifier = Modifier.fillMaxWidth(0.8f) // Button width is 80% of available width.
                    ) {
                        Text(
                            text = stringResource(R.string.favorite), // Text displayed on the button.
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold // Makes button text bold.
                            )
                        )
                    }

                    // Displays the detailed description of the podcast.
                    Text(
                        text = currentPodcast.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant // Uses a less prominent color.
                        ),
                        textAlign = TextAlign.Justify, // Justifies the text for a block-like appearance.
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
