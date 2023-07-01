package com.diogoandrebotas.librarycompanionwebapi.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val totalItems: Int,
    val items: List<GoogleBooksItem>
)

@Serializable
class GoogleBooksItem(
    val volumeInfo: GoogleBooksVolumeInfo
)

@Serializable
class GoogleBooksVolumeInfo(
    val title: String,
    val authors: List<String>,
    val pageCount: Int,
    val imageLinks: GoogleBooksImageLinks
)

@Serializable
class GoogleBooksImageLinks(
    val thumbnail: String
)
