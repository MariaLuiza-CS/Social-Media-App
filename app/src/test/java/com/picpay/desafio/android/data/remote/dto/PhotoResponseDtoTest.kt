package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import kotlin.test.Test

class PhotoResponseDtoTest {
    @Test
    fun `photo dto copy and equality works correctly`() {
        val photo1 = PhotoResponseDto(download_url = "https://example.com/photo1.jpg")
        val photo2 = photo1.copy(download_url = "https://example.com/photo2.jpg")

        assertEquals("https://example.com/photo2.jpg", photo2.download_url)
        assertNotEquals(photo1, photo2)
    }

    @Test
    fun `photo dto serialization and deserialization works correctly`() {
        val photo = PhotoResponseDto(download_url = "https://example.com/photo.jpg")
        val json = Json.encodeToString(photo)
        val deserialized = Json.decodeFromString<PhotoResponseDto>(json)

        assertEquals(photo, deserialized)
    }

    @Test
    fun `photo dto default values are null`() {
        val photo = PhotoResponseDto()
        assertNull(photo.download_url)
    }
}
