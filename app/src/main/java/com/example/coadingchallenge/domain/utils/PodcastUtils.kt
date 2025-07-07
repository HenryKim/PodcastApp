package com.example.coadingchallenge.domain.utils

import com.example.coadingchallenge.data.db.entity.PodcastEntity
import com.example.coadingchallenge.domain.model.Pod

fun Pod.toPodcastEntity(): PodcastEntity {
    return PodcastEntity(
        podCastId = id,
        title = title,
        publisher = publisher,
        thumbnailUrl = thumbnail,
        description = description,
        imageUrl = image
    )
}

fun PodcastEntity.toPod(): Pod {
    return Pod(
      id = podCastId,
      title = title,
      publisher = publisher,
      thumbnail = thumbnailUrl,
      description = description,
      image = imageUrl,
      isFavorite = false,
      rss = "",
      type = "",
      email = "",
      extra = null,
      genreIds = listOf(),
      itunesId = 0,
      website = "",
      language = "",
      country = "",
      latestPubDateMs = 0,
      earliestPubDateMs = 0,
      hasGuestInterviews = false,
      updateFrequencyHours = 0,
      listenScoreGlobalRank = "",
      totalEpisodes = 0,
      audioLengthSec = 0,
      explicitContent = false,
      hasSponsors = false,
      isClaimed = false,
      latestEpisodeId = "",
      listenNotesUrl = "",
      lookingFor = null,
      listenScore = 0
    )
}