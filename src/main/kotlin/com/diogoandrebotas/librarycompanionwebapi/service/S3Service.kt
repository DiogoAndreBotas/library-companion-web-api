package com.diogoandrebotas.librarycompanionwebapi.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import org.springframework.stereotype.Service
import java.util.*

@Service
class S3Service(
    private val client: S3Client
) {
    companion object {
        const val BUCKET_NAME = "library-companion-api-images"
    }

    suspend fun uploadImage(isbn: String, byteArray: ByteArray) {
        // TODO: Check if putObject is an upsert
        // If not, I might want to support that in the future to replace covers
        // Or fetch them from somewhere else

        val request = PutObjectRequest {
            bucket = BUCKET_NAME
            key = "${isbn}.jpg"
            metadata = mutableMapOf()
            body = ByteStream.fromBytes(byteArray)
        }

        client.putObject(request)
    }

    suspend fun getImage(isbn: String): Optional<ByteArray> {
        val request = GetObjectRequest {
            bucket = BUCKET_NAME
            key = "${isbn}.jpg"
        }

        // TODO: Handle key not found
        return client.getObject(request) {
            if (it.body == null) {
                Optional.empty()
            } else {
                Optional.of(it.body!!.toByteArray())
            }
        }
    }
}