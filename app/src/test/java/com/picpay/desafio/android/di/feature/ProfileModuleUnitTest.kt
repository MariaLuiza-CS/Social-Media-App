package com.picpay.desafio.android.di.feature

import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.PersonWithPhotos
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.presentation.profile.ProfileEvent
import com.picpay.desafio.android.presentation.profile.ProfileViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ProfileModuleUnitTest {
    private lateinit var viewModel: ProfileViewModel

    private val localCurrentUseCase: GetLocalCurrentUseCase = mock()
    private val contactUsersUseCase: GetContactUsersUseCase = mock()
    private val peopleWithPhotosUseCase: GetPeopleWithPhotosUseCase = mock()

    @Before
    fun setup() {

        whenever(localCurrentUseCase()).thenReturn(
            flow { emit(Result.Success(ContactUser("1", "Alice", "alice01", "img.jpg"))) }
        )
        whenever(contactUsersUseCase()).thenReturn(
            flow { emit(Result.Success(listOf(User("2", "Bob", "bob01")))) }
        )
        whenever(peopleWithPhotosUseCase()).thenReturn(
            flow { emit(Result.Success(listOf(PersonWithPhotos("3", "Charlie")))) }
        )

        viewModel = ProfileViewModel(
            getGetLocalCurrentUseCase = localCurrentUseCase,
            getContactUsersUseCase = contactUsersUseCase,
            getPeopleWithPhotosUseCase = peopleWithPhotosUseCase,
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `loadCurrentUser updates uiState`() = runTest {
        viewModel.onEvent(ProfileEvent.LoadCurrentUser)
        kotlinx.coroutines.delay(100)
        val state = viewModel.uiState.value
        assertEquals("Alice", state.currentUser?.name)
    }

    @Test
    fun `loadFollowersList updates uiState`() = runTest {
        viewModel.onEvent(ProfileEvent.LoadContactUserList)
        kotlinx.coroutines.delay(100)
        val state = viewModel.uiState.value
        assertEquals("Bob", state.contactUsersList.first()?.name)
    }

    @Test
    fun `loadPeopleWithPhotoList updates uiState`() = runTest {
        viewModel.onEvent(ProfileEvent.LoadPeopleWithPhotoList)
        kotlinx.coroutines.delay(100)
        val state = viewModel.uiState.value
        assertEquals("Charlie", state.peopleWithPhotosList.first()?.fistName)
    }
}
