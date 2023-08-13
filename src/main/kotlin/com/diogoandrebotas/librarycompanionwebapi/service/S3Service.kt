package com.diogoandrebotas.librarycompanionwebapi.service

import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import com.diogoandrebotas.librarycompanionwebapi.config.S3Config
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val s3Config: S3Config
) {
    companion object {
        const val BUCKET_NAME = "library-companion-api-images"
    }

    suspend fun uploadImage(isbn: String, byteArray: ByteArray) {
        val request = PutObjectRequest {
            bucket = BUCKET_NAME
            key = "${isbn}.jpg"
            metadata = mutableMapOf()
            body = ByteStream.fromBytes(byteArray)
        }

        s3Config.client().use { it.putObject(request) }
    }
}