package com.example.coadingchallenge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookingFor(
    val guests: Boolean,
    val cohosts: Boolean,
    val sponsors: Boolean,
    @SerialName("cross_promotion")
    val crossPromotion: Boolean,
)