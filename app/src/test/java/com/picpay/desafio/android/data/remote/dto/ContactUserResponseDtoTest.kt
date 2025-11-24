package com.picpay.desafio.android.data.remote.dto

import junit.framework.TestCase.assertNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ContactUserResponseDtoTest {

    @Test
    fun `dto copy and equality works correctly`() {
        val dto1 = ContactUserResponseDto(
            id = "1",
            name = "Ada",
            img = "https://example.com/ada.jpg",
            username = "ada123"
        )

        val dto2 = dto1.copy(name = "Bell")

        assertEquals("1", dto2.id)
        assertEquals("Bell", dto2.name)
        assertEquals(dto1.img, dto2.img)
        assertEquals(dto1.username, dto2.username)
        assertNotEquals(dto1, dto2)
    }

    @Test
    fun `dto serialization and deserialization works correctly`() {
        val dto = ContactUserResponseDto(
            id = "1",
            name = "Ada",
            img = "https://example.com/ada.jpg",
            username = "ada123"
        )

        val json = Json.encodeToString(dto)
        val deserialized = Json.decodeFromString<ContactUserResponseDto>(json)

        assertEquals(dto, deserialized)
    }

    @Test
    fun `dto default values are correct`() {
        val dto = ContactUserResponseDto()
        assertNull(dto.id)
        assertNull(dto.name)
        assertNull(dto.img)
        assertNull(dto.username)
    }
}
