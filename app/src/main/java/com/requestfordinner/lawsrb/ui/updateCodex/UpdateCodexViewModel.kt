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
 * This is a child class of [ViewModel] which is used in [UpdateCodexFragment].
 * It contain methods which helps to work with update buttons.
 *
 * @see ViewModel
 * @see [UpdateCodexFragment]
 */
class UpdateCodexViewModel : ViewModel() {

    private val TAG = "UpdateCodexViewModel"

    private var _uiState = MutableLiveData(UpdateCodexUiState())

    /** */
    val uiState: LiveData<UpdateCodexUiState> = _uiState

    init {
        updateIsUpdateEnabled()
    }

    /** */
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

    /** */
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
                    states = listOf(Pair(codex, ButtonState.UPDATING))
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
                        states = listOf(Pair(codex, ButtonState.ENABLED))
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
                        states = listOf(Pair(codex, ButtonState.DISABLED))
                    )
                )
            }
        }
    }

    private fun getUpdatingMessage(codex: Codex): Int {
        return when (codex) {
            Codex.UK -> R.string.UK_updating
            Codex.UPK -> R.string.UPK_updating
            Codex.KoAP -> R.string.KoAP_updating
            Codex.PIKoAP -> R.string.PIKoAP_updating
        }
    }

    private fun getUpdatedMessage(codex: Codex): Int {
        return when (codex) {
            Codex.UK -> R.string.UK_updated
            Codex.UPK -> R.string.UPK_updated
            Codex.KoAP -> R.string.KoAP_updated
            Codex.PIKoAP -> R.string.PIKoAP_updated
        }
    }

    /** This method update information about codex's changes */
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

    private fun getButtonStateByChanges(codex: Codex): ButtonState {
        return if (CodexVersionParser.isHaveChanges(codex)) ButtonState.ENABLED
        else ButtonState.DISABLED
    }
}
