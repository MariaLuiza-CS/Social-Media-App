package com.picpay.desafio.android.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `user should hold given values`() {
        val user = User(
            id = "1843",
            name = "Ada Lovelace",
            username = "ada1843",
            img = "ada/lovelace/img.jpg"
        )

        assertEquals("1843", user.id)
        assertEquals("Ada Lovelace", user.name)
        assertEquals("ada1843", user.username)
        assertEquals("ada/lovelace/img.jpg", user.img)
    }

    @Test
    fun `user default constructor should have null fields`() {
        val user = User()

        assertEquals(null, user.id)
        assertEquals(null, user.name)
        assertEquals(null, user.img)
        assertEquals(null, user.username)
    }
}
