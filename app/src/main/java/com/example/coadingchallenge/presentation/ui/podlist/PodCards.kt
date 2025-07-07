package com.example.coadingchallenge.presentation.ui.podlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.coadingchallenge.R
import com.example.coadingchallenge.domain.model.Pod

@Preview(showBackground = true)
@Composable
fun ShowPodCardsPreview() {
    var pod = Pod(
        id = "1",
        rss = "rss",
        type = "type",
        email = "email",
        extra = null,
        image = "https://example.com/image.jpg",
        title = "Very Long Podcast Title That Might Overflow",
        country = "country",
        website = "website",
        language = "language",
        genreIds = listOf(1, 2, 3),
        itunesId = 1234567890,
        publisher = "Famous Publisher Name",
        thumbnail = "https://example.com/thumbnail.jpg",
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PodImage(podcast = podcast)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Display "Favorited" text if the podcast is marked as a favorite
            if (podcast.isFavorite) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.favorited),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PodImage(
    podcast: Pod,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(80.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = podcast.image,
            contentDescription = stringResource(R.string.contentDescription_podcastThumnails),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
