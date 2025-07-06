package com.example.coadingchallenge.feature.ui.podlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.coadingchallenge.network.model.Pod

@Preview
@Composable
fun ShowPodCards() {
    val pod = Pod(
        id = "1",
        rss = "rss",
        type = "type",
        email = "email",
        extra = null,
        image = "image",
        title = "title",
        country = "country",
        website = "website",
        language = "language",
        genreIds = listOf(1, 2, 3),
        itunesId = 1234567890,
        publisher = "publisher",
        thumbnail = "thumbnail",
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
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.Top
    ){
        PodImage(podcast)
        Column {
            Text(
                text = podcast.title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = podcast.publisher,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 2.dp, start = 10.dp, end = 10.dp)
            )
        }
    }
}

@Composable
fun PodImage(podcast: Pod) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp),
        shape = MaterialTheme.shapes.medium // Added Shape for Rounded Corners
    ) {
        AsyncImage(
            model = podcast.image, // Use Podcast Url Directly
            contentDescription = podcast.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
        )
    }
}