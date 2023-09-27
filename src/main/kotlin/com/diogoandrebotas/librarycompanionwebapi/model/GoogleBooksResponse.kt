package com.diogoandrebotas.librarycompanionwebapi.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val totalItems: Int,
    val items: List<GoogleBooksItem> = emptyList()
)

@Serializable
data class GoogleBooksItem(
    val volumeInfo: GoogleBooksVolumeInfo
)

@Serializable
data class GoogleBooksVolumeInfo(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val pageCount: Int,
    val imageLinks: GoogleBooksImageLinks
)

@Serializable
data class GoogleBooksImageLinks(
    val thumbnail: String
)