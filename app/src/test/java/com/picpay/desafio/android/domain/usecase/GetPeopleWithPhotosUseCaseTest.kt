package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetPeopleWithPhotosUseCaseTest {

    private val peopleRepository: PeopleRepository = mockk()
    private val getPeopleWithPhotosUseCase = GetPeopleWithPhotosUseCase(peopleRepository)

    @Test
    fun `when invoke is called should call getPeople() from PeopleRepository`() = runTest {
        val fakePeopleList = listOf(
            PersonWithPhotos(
                "1",
                "Charlie",
                "Brown",
                "",
                "",
                "",
                null,
                emptyList()
            )
        )

        val expectedFlow: Flow<Result<List<PersonWithPhotos?>>> =
            flowOf(Result.Success(fakePeopleList))

        coEvery {
            peopleRepository.getPeople()
        } returns expectedFlow

        val resultFlow = getPeopleWithPhotosUseCase()

        coVerify(exactly = 1) {
            peopleRepository.getPeople()
        }

        assertSame(expectedFlow, resultFlow)
    }
}