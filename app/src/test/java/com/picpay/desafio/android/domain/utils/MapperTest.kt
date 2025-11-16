package com.picpay.desafio.android.domain.utils

import com.picpay.desafio.android.data.local.UserEntity
import com.picpay.desafio.android.data.remote.dto.UserResponseDto
import com.picpay.desafio.android.domain.util.toUser
import com.picpay.desafio.android.domain.util.toUserEntity
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun `UserEntity to User should map all fields correctly`() {
        val userEntity = UserEntity(
            id = "1843",
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = userEntity.toUser()

        assertEquals(userEntity.id, result.id)
        assertEquals(userEntity.name, result.name)
        assertEquals(userEntity.username, result.username)
        assertEquals(userEntity.img, result.img)
    }

    @Test
    fun `UserResponseDto to UserEntity should map all fields correctly`() {
        val userResponseDto = UserResponseDto(
            id = "1843",
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = userResponseDto.toUserEntity()

        assertEquals(userResponseDto.id, result.id)
        assertEquals(userResponseDto.name, result.name)
        assertEquals(userResponseDto.username, result.username)
        assertEquals(userResponseDto.img, result.img)
    }

    @Test
    fun `UserResponseDto to UserEntity should convert null id to empty string`() {
        val userResponseDto = UserResponseDto(
            id = null,
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        val result = userResponseDto.toUserEntity()

        assertEquals("", result.id)
        assertEquals(userResponseDto.name, result.name)
        assertEquals(userResponseDto.username, result.username)
        assertEquals(userResponseDto.img, result.img)
    }
}
