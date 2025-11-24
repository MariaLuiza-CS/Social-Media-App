package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import kotlin.test.Test

class PeopleResponseDtoTest {
    @Test
    fun `people dto copy and equality works correctly`() {
        val person = PersonResponseDto(
            gender = "female",
            name = NameResponseDto(first = "Ada", last = "Lovelace"),
            email = "ada@example.com",
            picture = PictureResponseDto(large = "https://example.com/ada.jpg"),
            login = LoginResponseDto(uuid = "uuid-123")
        )

        val dto1 = PeopleResponseDto(results = listOf(person))
        val dto2 = dto1.copy(results = emptyList())

        assertNotEquals(dto1, dto2)
        assertEquals(1, dto1.results?.size)
        assertTrue(dto2.results?.isEmpty() == true)
    }

    @Test
    fun `people dto serialization and deserialization works correctly`() {
        val person = PersonResponseDto(
            gender = "female",
            name = NameResponseDto(first = "Ada", last = "Lovelace"),
            email = "ada@example.com",
            picture = PictureResponseDto(large = "https://example.com/ada.jpg"),
            login = LoginResponseDto(uuid = "uuid-123")
        )

        val dto = PeopleResponseDto(results = listOf(person))

        val json = Json.encodeToString(dto)
        val deserialized = Json.decodeFromString<PeopleResponseDto>(json)

        assertEquals(dto, deserialized)
    }

    @Test
    fun `people dto default values are null`() {
        val dto = PeopleResponseDto()
        assertNull(dto.results)
    }
}
