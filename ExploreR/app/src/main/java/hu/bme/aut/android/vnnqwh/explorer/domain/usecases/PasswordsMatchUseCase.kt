package hu.bme.aut.android.vnnqwh.explorer.domain.usecases

class PasswordsMatchUseCase {

    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}