package com.example.coadingchallenge.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Extra(
    val url1: String,
    val url2: String,
    val url3: String,
    @SerialName("spotify_url")
    val spotifyUrl: String,
    @SerialName("youtube_url")
    val youtubeUrl: String,
    @SerialName("linkedin_url")
    val linkedinUrl: String,
    @SerialName("wechat_handle")
    val wechatHandle: String,
    @SerialName("patreon_handle")
    val patreonHandle: String,
    @SerialName("twitter_handle")
    val twitterHandle: String,
    @SerialName("facebook_handle")
    val facebookHandle: String,
    @SerialName("amazon_music_url")
    val amazonMusicUrl: String,
    @SerialName("instagram_handle")
    val instagramHandle: String,
)