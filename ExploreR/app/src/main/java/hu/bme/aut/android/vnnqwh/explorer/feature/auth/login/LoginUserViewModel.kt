    package hu.bme.aut.android.vnnqwh.explorer.feature.auth.login

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import dagger.hilt.android.lifecycle.HiltViewModel
    import hu.bme.aut.android.vnnqwh.explorer.R
    import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
    import hu.bme.aut.android.vnnqwh.explorer.domain.usecases.IsEmailValidUseCase
    import hu.bme.aut.android.vnnqwh.explorer.ui.model.UiText
    import hu.bme.aut.android.vnnqwh.explorer.ui.model.toUiText
    import hu.bme.aut.android.vnnqwh.explorer.util.UiEvent
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.channels.Channel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.receiveAsFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class LoginUserViewModel @Inject constructor(
        private val authService: AuthService,
        private val isEmailValid: IsEmailValidUseCase
    ): ViewModel() {

        private val _state = MutableStateFlow(LoginUserState())
        val state = _state.asStateFlow()

        private val email get() = state.value.email

        private val password get() = state.value.password

        private val _uiEvent = Channel<UiEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        fun onEvent(event: LoginUserEvent) {
            when(event) {
                is LoginUserEvent.EmailChanged -> {
                    val newEmail = event.email.trim()
                    _state.update { it.copy(email = newEmail) }
                }
                is LoginUserEvent.PasswordChanged -> {
                    val newPassword = event.password.trim()
                    _state.update { it.copy(password = newPassword) }
                }
                LoginUserEvent.PasswordVisibilityChanged -> {
                    _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
                }
                LoginUserEvent.SignIn -> {
                    onSignIn()
                }
            }
        }

        private fun onSignIn() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (!isEmailValid(email)) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                        )
                    } else {
                        if (password.isBlank()) {
                            _uiEvent.send(
                                UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                            )
                        } else {
                            authService.authenticate(email,password)
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                } catch (e: Exception) {
                    _uiEvent.send(UiEvent.Failure(e.toUiText()))
                }
            }
        }
    }


    data class LoginUserState(
        val email: String = "",
        val password: String = "",
        val passwordVisibility: Boolean = false
    )

    sealed class LoginUserEvent {
        data class EmailChanged(val email: String): LoginUserEvent()
        data class PasswordChanged(val password: String): LoginUserEvent()
        object PasswordVisibilityChanged: LoginUserEvent()
        object SignIn: LoginUserEvent()
    }