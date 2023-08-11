package com.diogoandrebotas.librarycompanionwebapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibraryBookResponse(
    val title: String,
    val authors: List<OpenLibraryBookAuthorResponse>,
    @SerialName("publish_date")
    val publishDate: String,
    val covers: List<Int>,
    @SerialName("number_of_pages")
    val numberOfPages: Int
)

@Serializable
class OpenLibraryBookAuthorResponse(
    val key: String
)
