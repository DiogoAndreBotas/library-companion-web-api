package com.diogoandrebotas.librarycompanionwebapi.service

import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
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

    suspend fun getImage(isbn: String): ByteArray? {
        val request = GetObjectRequest {
            bucket = BUCKET_NAME
            key = "${isbn}.jpg"
        }

        return s3Config.client().use { client ->
            client.getObject(request) {
                it.body?.toByteArray()
            }
        }
    }
}