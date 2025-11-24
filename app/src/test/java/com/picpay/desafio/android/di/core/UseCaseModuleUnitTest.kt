package com.picpay.desafio.android.di.core

import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.data.repository.ContactUserRepository
import com.picpay.desafio.android.data.repository.PeopleRepository
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Photo
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class UseCaseModuleUnitTest {
    private lateinit var contactUserRepository: ContactUserRepository
    private lateinit var userRepository: UserRepository
    private lateinit var peopleRepository: PeopleRepository

    private lateinit var getContactUsersUseCase: GetContactUsersUseCase
    private lateinit var getLocalCurrentUseCase: GetLocalCurrentUseCase
    private lateinit var getPeopleWithPhotosUseCase: GetPeopleWithPhotosUseCase
    private lateinit var signInWithGoogleUseCase: SignInWithGoogleUseCase

    @Before
    fun setup() {
        contactUserRepository = mock()
        userRepository = mock()
        peopleRepository = mock()

        getContactUsersUseCase = GetContactUsersUseCase(contactUserRepository)
        getLocalCurrentUseCase = GetLocalCurrentUseCase(userRepository)
        getPeopleWithPhotosUseCase = GetPeopleWithPhotosUseCase(peopleRepository)
        signInWithGoogleUseCase = SignInWithGoogleUseCase(userRepository)
    }

    @Test
    fun `GetContactUsersUseCase emits Success with list of users`() = runTest {
        val fakeUsers = listOf(
            User("1", "Alice", "alice01", "img1.jpg"),
            User("2", "Bob", "bob02", "img2.jpg")
        )
        whenever(contactUserRepository.getContactUsers()).thenReturn(flow {
            emit(
                Result.Success(
                    fakeUsers
                )
            )
        })

        val emissions = getContactUsersUseCase().toList()
        val result = emissions.first()

        assertTrue(result is com.picpay.desafio.android.domain.model.Result.Success)
        assertEquals(
            2,
            (result as com.picpay.desafio.android.domain.model.Result.Success).data.size
        )
    }

    @Test
    fun `GetLocalCurrentUseCase emits Success with current user`() = runTest {
        val fakeUser = ContactUser("uid123", "Test User", "testuser", "img.jpg")
        whenever(userRepository.getLocalCurrentUser()).thenReturn(flow {
            emit(
                com.picpay.desafio.android.domain.model.Result.Success(
                    fakeUser
                )
            )
        })

        val emissions = getLocalCurrentUseCase().toList()
        val result = emissions.first()

        assertTrue(result is com.picpay.desafio.android.domain.model.Result.Success)
        assertEquals(
            "uid123",
            (result as com.picpay.desafio.android.domain.model.Result.Success).data?.id
        )
    }

    @Test
    fun `GetPeopleWithPhotosUseCase emits Success with list of people`() = runTest {
        val fakePeople: List<PersonWithPhotos?> = listOf(
            PersonWithPhotos(
                id = "uuid-123",
                fistName = "Ada",
                lastName = "Lovelace",
                title = "Ms",
                gender = "female",
                email = "ada@example.com",
                profilePicture = "url_image",
                photos = listOf(Photo("url_photo1"))
            )
        )
        Mockito.`when`(peopleRepository.getPeople())
            .thenReturn(flow {
                emit(
                    Result.Success(
                        fakePeople
                    )
                )
            })

        val emissions = getPeopleWithPhotosUseCase().toList()
        val result = emissions.first()

        assertTrue(result is Result.Success)
        val people = (result as Result.Success).data
        assertEquals(1, people.size)
        assertEquals("Ada", people.first()?.fistName)
    }

    @Test
    fun `SignInWithGoogleUseCase emits Success with FirebaseUser`() = runTest {
        val fakeFirebaseUser: FirebaseUser = mock()
        whenever(userRepository.signInWithGoogle("fake_token")).thenReturn(flow {
            emit(
                Result.Success(
                    fakeFirebaseUser
                )
            )
        })

        val emissions = signInWithGoogleUseCase("fake_token").toList()
        val result = emissions.first()

        assertTrue(result is com.picpay.desafio.android.domain.model.Result.Success)
        assertEquals(
            fakeFirebaseUser,
            (result as com.picpay.desafio.android.domain.model.Result.Success).data
        )
    }
}
