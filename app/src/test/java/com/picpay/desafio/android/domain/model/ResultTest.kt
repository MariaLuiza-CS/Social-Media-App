package com.picpay.desafio.android.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultTest {
    @Test
    fun `Success should hold correct data`() {
        val result: Result<Int> = Result.Success(1843)

        assertTrue(result is Result.Success)
        assertEquals(1843, result.data)
    }

    @Test
    fun `Error should hold exception and message`() {
        val exception = IllegalStateException("Error")
        val result: Result<Nothing> = Result.Error(
            exception = exception,
            message = "Something went wrong"
        )

        assertTrue(result is Result.Error)
        assertEquals(exception, result.exception)
        assertEquals("Something went wrong", result.message)
    }

    @Test
    fun `Loading should be singleton`() {
        val first = Result.Loading
        val second = Result.Loading

        assertTrue(first === second)
    }
}
