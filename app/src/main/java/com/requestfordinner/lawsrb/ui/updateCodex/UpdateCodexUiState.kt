package com.requestfordinner.lawsrb.ui.updateCodex

import com.requestfordinner.lawsrb.basic.htmlParser.Codex

/**
 *
 */
data class UpdateCodexUiState(
    /** */
    val UKButtonState: ButtonState = ButtonState.DISABLED,
    /** */
    val UPKButtonState: ButtonState = ButtonState.DISABLED,
    /** */
    val KoAPButtonState: ButtonState = ButtonState.DISABLED,
    /** */
    val PIKoAPButtonState: ButtonState = ButtonState.DISABLED,
    /**  */
    val checkUpdatesButtonState: ButtonState = ButtonState.ENABLED,

    /** */
    var messageToShow: Int? = null
) {

    /** */
    enum class ButtonState {
        /** */
        DISABLED,

        /** */
        ENABLED,

        /** */
        UPDATING
    }

    fun isUpdateEnabled(): Boolean {
        return UKButtonState == ButtonState.ENABLED ||
                UPKButtonState == ButtonState.ENABLED ||
                KoAPButtonState == ButtonState.ENABLED ||
                PIKoAPButtonState == ButtonState.ENABLED
    }

    /** */
    fun getState(codex: Codex): ButtonState {
        return when (codex) {
            Codex.UK -> UKButtonState
            Codex.UPK -> UPKButtonState
            Codex.KoAP -> KoAPButtonState
            Codex.PIKoAP -> PIKoAPButtonState
        }
    }

    /** */
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