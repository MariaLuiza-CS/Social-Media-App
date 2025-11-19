package com.picpay.desafio.android.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        savedStateHandle.get<LoginUiState>("loginUiState") ?: LoginUiState()
    )
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.collect { newState ->
                savedStateHandle["loginUiState"] = newState
            }
        }
    }

    fun onEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.SignInGoogle -> signInWithGoogle(loginEvent.idToken)
        }
    }

    private suspend fun sendEffect(effect: LoginEffect) {
        _effect.emit(effect)
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            signInWithGoogleUseCase(idToken = idToken).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        sendEffect(
                            LoginEffect.NavigateToHome
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        sendEffect(
                            LoginEffect.NavigateToError(
                                result.message ?: "Unknown error"
                            )
                        )
                    }
                }

            }
        }
    }
}