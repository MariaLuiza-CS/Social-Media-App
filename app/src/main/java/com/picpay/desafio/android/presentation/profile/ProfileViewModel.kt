package com.picpay.desafio.android.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.usecase.GetContactUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetLocalCurrentUseCase
import com.picpay.desafio.android.domain.usecase.GetPeopleWithPhotosUseCase
import com.picpay.desafio.android.domain.usecase.SignOutGoogleUseCase
import com.picpay.desafio.android.presentation.main.MainEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getGetLocalCurrentUseCase: GetLocalCurrentUseCase,
    private val getContactUsersUseCase: GetContactUsersUseCase,
    private val getPeopleWithPhotosUseCase: GetPeopleWithPhotosUseCase,
    private val signOutGoogleUseCase: SignOutGoogleUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        savedStateHandle.get<ProfileUiState>("profileUiState") ?: ProfileUiState()
    )
    var uiState: StateFlow<ProfileUiState> = _uiState

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            uiState.collect { newState ->
                savedStateHandle["profileUiState"] = newState
            }
        }
    }

    private suspend fun sendEffect(effect: ProfileEffect) {
        _effect.emit(effect)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadCurrentUser -> {
                loadCurrentUser()
            }

            is ProfileEvent.LoadContactUserList -> {
                loadFollowersList()
            }

            is ProfileEvent.LoadPeopleWithPhotoList -> {
                loadPeopleWithPhotoList()
            }

            is ProfileEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutGoogleUseCase()
            sendEffect(
                ProfileEffect.NavigateToLogin
            )
        }
    }

    private fun loadPeopleWithPhotoList() {
        viewModelScope.launch {
            getPeopleWithPhotosUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message ?: "error to get current user"
                        )
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            peopleWithPhotosList = result.data
                        )
                    }
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getGetLocalCurrentUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message ?: "error to get current user"
                        )
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            currentUser = result.data
                        )
                    }
                }
            }
        }
    }

    private fun loadFollowersList() {
        viewModelScope.launch {
            getContactUsersUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message ?: "error to get current user"
                        )
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            contactUsersList = result.data
                        )
                    }
                }
            }
        }
    }
}
