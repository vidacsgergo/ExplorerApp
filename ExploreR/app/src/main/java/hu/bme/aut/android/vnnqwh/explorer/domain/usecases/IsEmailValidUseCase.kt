package hu.bme.aut.android.vnnqwh.explorer.domain.usecases

import javax.inject.Inject

class IsEmailValidUseCase @Inject constructor() {

    operator fun invoke(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

}