package com.example.coadingchallenge.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pod(
    val id: String,
    val rss: String,
    val type: String,
    val email: String,
    val extra: Extra?,
    val image: String,
    val title: String,
    val country: String,
    val website: String,
    val language: String,
    @SerialName("genre_ids")
    val genreIds: List<Long>,
    @SerialName("itunes_id")
    val itunesId: Long,
    val publisher: String,
    val thumbnail: String,
    @SerialName("is_claimed")
    val isClaimed: Boolean,
    val description: String,
    @SerialName("looking_for")
    val lookingFor: LookingFor?,
    @SerialName("has_sponsors")
    val hasSponsors: Boolean,
    @SerialName("listen_score")
    val listenScore: Long,
    @SerialName("total_episodes")
    val totalEpisodes: Long,
    @SerialName("listennotes_url")
    val listenNotesUrl: String,
    @SerialName("audio_length_sec")
    val audioLengthSec: Long,
    @SerialName("explicit_content")
    val explicitContent: Boolean,
    @SerialName("latest_episode_id")
    val latestEpisodeId: String,
    @SerialName("latest_pub_date_ms")
    val latestPubDateMs: Long,
    @SerialName("earliest_pub_date_ms")
    val earliestPubDateMs: Long,
    @SerialName("has_guest_interviews")
    val hasGuestInterviews: Boolean,
    @SerialName("update_frequency_hours")
    val updateFrequencyHours: Long,
    @SerialName("listen_score_global_rank")
    val listenScoreGlobalRank: String,
)


data class PodsContainer(
    val data: List<Pod>?,
    val url: String,
)