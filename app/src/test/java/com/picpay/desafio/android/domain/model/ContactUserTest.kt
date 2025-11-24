package com.picpay.desafio.android.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactUserTest {
    @Test
    fun createContactUser_shouldHoldCorrectValues() {
        val contact = ContactUser(
            id = "1",
            name = "João",
            email = "joao@email.com",
            img = "url_image"
        )

        assertEquals("1", contact.id)
        assertEquals("João", contact.name)
        assertEquals("joao@email.com", contact.email)
        assertEquals("url_image", contact.img)
    }

    @Test
    fun dataClass_equalsShouldWork() {
        val c1 = ContactUser("1", "A", "a@a.com", "img")
        val c2 = ContactUser("1", "A", "a@a.com", "img")

        assertEquals(c1, c2)
    }
}
