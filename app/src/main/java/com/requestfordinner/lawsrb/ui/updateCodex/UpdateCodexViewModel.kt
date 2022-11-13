package com.requestfordinner.lawsrb.ui.updateCodex

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.NetworkCheck
import com.requestfordinner.lawsrb.basic.Preferences
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.htmlParser.CodexParser
import com.requestfordinner.lawsrb.basic.htmlParser.CodexVersionParser
import com.requestfordinner.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.requestfordinner.lawsrb.ui.updateCodex.UpdateCodexUiState.ButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is a child class of [ViewModel] which manage [UpdateCodexFragment].
 *
 * @see [UpdateCodexFragment]
 */
class UpdateCodexViewModel : ViewModel() {

    private val TAG = "UpdateCodexViewModel"

    private var _uiState = MutableLiveData(UpdateCodexUiState())

    /**
     * The state of [UpdateCodexFragment].
     *
     * @see [UpdateCodexUiState]
     */
    val uiState: LiveData<UpdateCodexUiState> = _uiState

    init {
        updateIsUpdateEnabled()
    }

    /** Check codexes for updates. If there are then updates buttons state. */
    fun checkCodexUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkCheck.isNotAvailable) {
                _uiState.postValue(
                    _uiState.value?.copy(messageToShow = R.string.no_network_access)
                )
                return@launch
            }

            _uiState.postValue(
                _uiState.value?.copy(
                    messageToShow = R.string.start_update_codex_check,
                    checkUpdatesButtonState = ButtonState.DISABLED
                )
            )

            CodexVersionParser.update().join()

            val message = if (CodexVersionParser.isHaveChanges()) {
                updateIsUpdateEnabled()
                R.string.codex_updates_available
            } else {
                R.string.no_codex_updates_available
            }

            _uiState.postValue(
                _uiState.value?.copy(
                    messageToShow = message,
                    checkUpdatesButtonState = ButtonState.ENABLED
                )
            )
        }
    }

    /** Execute updating for given [codex]. */
    fun executeCodexUpdating(codex: Codex) {
        if (NetworkCheck.isNotAvailable) {
            _uiState.postValue(
                _uiState.value?.copy(messageToShow = R.string.no_network_access)
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.postValue(
                _uiState.value?.copy(
                    messageToShow = getUpdatingMessage(codex),
                    states = mapOf(codex to ButtonState.UPDATING)
                )
            )

            kotlin.runCatching {
                val codexLists = CodexParser().get(codex)
                BaseCodexDatabase.update(codex, codexLists)
            }.onFailure {
                Log.e(TAG, "Internet connection interrupted: ${it.message}")
                _uiState.postValue(
                    _uiState.value?.copy(
                        messageToShow = R.string.internet_connection_interrupted,
                        states = mapOf(codex to ButtonState.ENABLED)
                    )
                )
            }.onSuccess {
                BaseCodexProvider.setDefaultPageItems()
                Preferences.setCodexInfo(
                    codex,
                    CodexVersionParser.getChangesCount(codex),
                    CodexVersionParser.getChangeDate(codex)
                )
                _uiState.postValue(
                    _uiState.value?.copy(
                        messageToShow = getUpdatedMessage(codex),
                        states = mapOf(codex to ButtonState.DISABLED)
                    )
                )
            }
        }
    }

    /** Returns a message about given [codex] **updating**. */
    private fun getUpdatingMessage(codex: Codex): Int {
        return when (codex) {
            Codex.UK -> R.string.UK_updating
            Codex.UPK -> R.string.UPK_updating
            Codex.KoAP -> R.string.KoAP_updating
            Codex.PIKoAP -> R.string.PIKoAP_updating
        }
    }

    /** Returns a message about **updated** [codex]. */
    private fun getUpdatedMessage(codex: Codex): Int {
        return when (codex) {
            Codex.UK -> R.string.UK_updated
            Codex.UPK -> R.string.UPK_updated
            Codex.KoAP -> R.string.KoAP_updated
            Codex.PIKoAP -> R.string.PIKoAP_updated
        }
    }

    /** Updates button state. */
    fun updateIsUpdateEnabled() {
        _uiState.postValue(
            _uiState.value?.copy(
                UKButtonState = getButtonStateByChanges(Codex.UK),
                UPKButtonState = getButtonStateByChanges(Codex.UPK),
                KoAPButtonState = getButtonStateByChanges(Codex.KoAP),
                PIKoAPButtonState = getButtonStateByChanges(Codex.PIKoAP),
                messageToShow = null
            )
        )
    }

    /** Returns [ButtonState] for given [codex] using [CodexVersionParser]. */
    private fun getButtonStateByChanges(codex: Codex): ButtonState {
        return if (CodexVersionParser.isHaveChanges(codex)) ButtonState.ENABLED
        else ButtonState.DISABLED
    }
}
