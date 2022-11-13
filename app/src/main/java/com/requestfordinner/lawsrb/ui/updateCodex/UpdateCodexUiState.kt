package com.requestfordinner.lawsrb.ui.updateCodex

import com.requestfordinner.lawsrb.basic.htmlParser.Codex

/**
 * UI state of [UpdateCodexFragment] page.
 *
 * @see [UpdateCodexFragment]
 * @see [UpdateCodexViewModel]
 */
data class UpdateCodexUiState(

    /**
     * *Criminal Codex* button state.
     *
     * @see ButtonState
     */
    val UKButtonState: ButtonState = ButtonState.DISABLED,

    /**
     * *Codex of Criminal Procedure* button state.
     *
     * @see ButtonState
     */
    val UPKButtonState: ButtonState = ButtonState.DISABLED,

    /**
     * *Codex of Administrative Offences* button state.
     *
     * @see ButtonState
     */
    val KoAPButtonState: ButtonState = ButtonState.DISABLED,

    /**
     * *Procedural and Executive Codex on Administrative Offenses* button state.
     *
     * @see ButtonState
     */
    val PIKoAPButtonState: ButtonState = ButtonState.DISABLED,

    /**
     * *Check updates* button state.
     *
     * @see ButtonState
     */
    val checkUpdatesButtonState: ButtonState = ButtonState.ENABLED,

    /** Message to show id. */
    var messageToShow: Int? = null
) {

    /** Represents different update button states. */
    enum class ButtonState {
        DISABLED, ENABLED, UPDATING
    }

    /** Returns `true` if at least one [ButtonState] equals [ButtonState.ENABLED]. */
    fun isUpdateEnabled(): Boolean {
        return UKButtonState == ButtonState.ENABLED ||
                UPKButtonState == ButtonState.ENABLED ||
                KoAPButtonState == ButtonState.ENABLED ||
                PIKoAPButtonState == ButtonState.ENABLED
    }

    /** Returns [ButtonState] for given [codex]. */
    fun getState(codex: Codex): ButtonState {
        return when (codex) {
            Codex.UK -> UKButtonState
            Codex.UPK -> UPKButtonState
            Codex.KoAP -> KoAPButtonState
            Codex.PIKoAP -> PIKoAPButtonState
        }
    }

    /** Returns copy of current object with [states] and [messageToShow] updated. */
    fun copy(
        states: Map<Codex, ButtonState>,
        messageToShow: Int? = this.messageToShow
    ): UpdateCodexUiState {
        return UpdateCodexUiState(
            UKButtonState = states[Codex.UK] ?: UKButtonState,
            UPKButtonState = states[Codex.UPK] ?: UPKButtonState,
            KoAPButtonState = states[Codex.KoAP] ?: KoAPButtonState,
            PIKoAPButtonState = states[Codex.PIKoAP] ?: PIKoAPButtonState,
            messageToShow = messageToShow
        )
    }
}