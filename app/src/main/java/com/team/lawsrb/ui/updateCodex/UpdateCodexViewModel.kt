package com.team.lawsrb.ui.updateCodex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexVersionParser

/**
 * This is a child class of [ViewModel] which is used in [UpdateCodexFragment].
 * It contain methods which helps to work with update buttons.
 *
 * @see ViewModel
 * @see [UpdateCodexFragment]
 */
class UpdateCodexViewModel : ViewModel() {

    /** Equals `true`, if UK codex have changes. */
    private val isUKUpdateEnabled = MutableLiveData<Boolean>()

    /** Equals `true`, if UPK codex have changes. */
    private val isUPKUpdateEnabled = MutableLiveData<Boolean>()

    /** Equals `true`, if KoAP codex have changes. */
    private val isKoAPUpdateEnabled = MutableLiveData<Boolean>()

    /** Equals `true`, if PIKoAP codex have changes. */
    private val isPIKoAPUpdateEnabled = MutableLiveData<Boolean>()

    var isCheckUpdateButtonEnabled = MutableLiveData<Boolean>()

    init {
        isCheckUpdateButtonEnabled.value = true
        updateIsUpdateEnabled()
    }

    /** This method return `true` if given [codex] have changes. */
    fun isUpdateEnabled(codex: Codex): MutableLiveData<Boolean>{
        return when(codex){
            Codex.UK -> isUKUpdateEnabled
            Codex.UPK -> isUPKUpdateEnabled
            Codex.KoAP -> isKoAPUpdateEnabled
            Codex.PIKoAP -> isPIKoAPUpdateEnabled
        }
    }

    /** This method return `true` if at least one codex have changes. */
    fun isUpdateEnabled(): Boolean{
        // '== true' because .value can be null
        return isUKUpdateEnabled.value == true ||
                isUPKUpdateEnabled.value == true ||
                isKoAPUpdateEnabled.value == true ||
                isPIKoAPUpdateEnabled.value == true
    }

    /** This method update information about codex's changes */
    fun updateIsUpdateEnabled(){
        isUKUpdateEnabled.postValue(CodexVersionParser.isHaveChanges(Codex.UK))
        isUPKUpdateEnabled.postValue(CodexVersionParser.isHaveChanges(Codex.UPK))
        isKoAPUpdateEnabled.postValue(CodexVersionParser.isHaveChanges(Codex.KoAP))
        isPIKoAPUpdateEnabled.postValue(CodexVersionParser.isHaveChanges(Codex.PIKoAP))
    }
}
