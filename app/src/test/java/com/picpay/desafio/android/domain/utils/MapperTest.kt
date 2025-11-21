package com.picpay.desafio.android.domain.utils

import com.picpay.desafio.android.data.local.entity.ContactUserEntity
import com.picpay.desafio.android.data.remote.dto.ContactUserResponseDto
import com.picpay.desafio.android.domain.util.toUser
import com.picpay.desafio.android.domain.util.toUserEntity
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun `UserEntity to User should map all fields correctly`() {
        val contactUserEntity = ContactUserEntity(
            id = "1843",
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = contactUserEntity.toUser()

        assertEquals(contactUserEntity.id, result.id)
        assertEquals(contactUserEntity.name, result.name)
        assertEquals(contactUserEntity.username, result.username)
        assertEquals(contactUserEntity.img, result.img)
    }

    @Test
    fun `UserResponseDto to UserEntity should map all fields correctly`() {
        val contactUserResponseDto = ContactUserResponseDto(
            id = "1843",
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = contactUserResponseDto.toUserEntity()

        assertEquals(contactUserResponseDto.id, result.id)
        assertEquals(contactUserResponseDto.name, result.name)
        assertEquals(contactUserResponseDto.username, result.username)
        assertEquals(contactUserResponseDto.img, result.img)
    }

    @Test
    fun `UserResponseDto to UserEntity should convert null id to empty string`() {
        val contactUserResponseDto = ContactUserResponseDto(
            id = null,
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = contactUserResponseDto.toUserEntity()

        assertEquals("", result.id)
        assertEquals(contactUserResponseDto.name, result.name)
        assertEquals(contactUserResponseDto.username, result.username)
        assertEquals(contactUserResponseDto.img, result.img)
    }
}
