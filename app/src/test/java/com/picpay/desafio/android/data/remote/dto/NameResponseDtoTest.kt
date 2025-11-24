package com.picpay.desafio.android.data.remote.dto

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import kotlin.test.Test

class NameResponseDtoTest {
    @Test
    fun `name dto copy and equality works correctly`() {
        val name1 = NameResponseDto(title = "Ms", first = "Ada", last = "Lovelace")
        val name2 = name1.copy(first = "Bell")

        assertEquals("Ms", name2.title)
        assertEquals("Bell", name2.first)
        assertNotEquals(name1, name2)
    }

    @Test
    fun `name dto serialization and deserialization works correctly`() {
        val name = NameResponseDto(title = "Ms", first = "Ada", last = "Lovelace")
        val json = Json.encodeToString(name)
        val deserialized = Json.decodeFromString<NameResponseDto>(json)

        assertEquals(name, deserialized)
    }

    @Test
    fun `name dto default values are null`() {
        val name = NameResponseDto()
        assertNull(name.title)
        assertNull(name.first)
        assertNull(name.last)
    }
}
