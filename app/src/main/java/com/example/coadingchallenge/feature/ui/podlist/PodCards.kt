package com.example.coadingchallenge.feature.ui.podlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth // Changed from fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size // For PodImage Card
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp // No longer needed if fontSize is only in styles
import coil3.compose.AsyncImage
import com.example.coadingchallenge.R
import com.example.coadingchallenge.network.model.Pod

@Preview(showBackground = true) // Added showBackground for better preview
@Composable
fun ShowPodCardsPreview() { // Renamed for clarity that it's a preview
    val pod = Pod(
        id = "1",
        rss = "rss",
        type = "type",
        email = "email",
        extra = null,
        image = "https://example.com/image.jpg", // Using a placeholder URL for AsyncImage
        title = "Very Long Podcast Title That Might Overflow",
        country = "country",
        website = "website",
        language = "language",
        genreIds = listOf(1, 2, 3),
        itunesId = 1234567890,
        publisher = "Famous Publisher Name",
        thumbnail = "https://example.com/thumbnail.jpg", // Using a placeholder URL
        isClaimed = true,
        description = "description",
        lookingFor = null,
        hasSponsors = true,
        listenScore = 100,
        totalEpisodes = 10,
        listenNotesUrl = "listenNotesUrl",
        audioLengthSec = 3600,
        explicitContent = true,
        latestEpisodeId = "latestEpisodeId",
        latestPubDateMs = 1234567890,
        earliestPubDateMs = 1234567890,
        hasGuestInterviews = true,
        updateFrequencyHours = 24,
        listenScoreGlobalRank = "listenScoreGlobalRank"
    )
    PodsCard(podcast = pod, onItemClick = {})
}

@Composable
fun PodsCard(
    podcast: Pod,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier // Allow passing external modifiers
) {
    Row(
        modifier = modifier // Use the passed modifier
            .fillMaxWidth() // Changed from fillMaxSize
            .clickable { onItemClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp), // Standard padding values
        verticalAlignment = Alignment.CenterVertically // Center items vertically for better look
    ) {
        PodImage(podcast = podcast)

        Spacer(modifier = Modifier.width(16.dp)) // Add space between image and text

        Column(
            modifier = Modifier.weight(1f) // Allow text to take available space and wrap
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // Handle long titles gracefully
            )

            Spacer(modifier = Modifier.height(4.dp)) // Add small space between title and publisher

            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.bodyMedium.copy( // Using bodyMedium for publisher
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Softer color
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis // Handle long publisher names gracefully
            )
        }
    }
}

@Composable
fun PodImage(
    podcast: Pod,
    modifier: Modifier = Modifier // Allow passing external modifiers
) {
    Card(
        modifier = modifier
            .size(80.dp), // Use .size() for equal width and height
        shape = MaterialTheme.shapes.medium,
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Optional: add subtle elevation
    ) {
        AsyncImage(
            model = podcast.image, // Use 'image' for main card, 'thumbnail' for smaller usually
            contentDescription = stringResource(R.string.contentDescription_podcastThumnails),
            contentScale = ContentScale.Crop, // Changed from FillBounds to avoid distortion
            modifier = Modifier.fillMaxSize() // Image should fill the Card
                .clip(MaterialTheme.shapes.medium) // Ensure image respects card's rounded corners
        )
    }
}
