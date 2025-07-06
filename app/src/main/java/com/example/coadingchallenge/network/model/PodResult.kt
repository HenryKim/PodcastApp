package com.example.coadingchallenge.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PodResult(
    val id: Long?,
    val name: String?,
    val total: Long?,
    @SerialName("has_next")
    val hasNext: Boolean?,
    val podcasts: List<Pod>?,
    @SerialName("parent_id")
    val parentId: Long?,
    @SerialName("page_number")
    val pageNumber: Long?,
    @SerialName("has_previous")
    val hasPrevious: Boolean?,
    @SerialName("listennotes_url")
    val listenNotesUrl: String?,
    @SerialName("next_page_number")
    val nextPageNumber: Long?,
    @SerialName("previous_page_number")
    val previousPageNumber: Long?,
)