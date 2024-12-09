package hu.bme.aut.android.vnnqwh.explorer.util

import hu.bme.aut.android.vnnqwh.explorer.ui.model.UiText

sealed class UiEvent {
    object Success: UiEvent()

    data class Failure(val message: UiText): UiEvent()
}
