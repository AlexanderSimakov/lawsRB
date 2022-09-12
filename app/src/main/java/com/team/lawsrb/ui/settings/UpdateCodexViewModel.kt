package com.team.lawsrb.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.htmlParser.CodexVersionParser

class UpdateCodexViewModel : ViewModel() {
    private val isUKUpdateEnabled = MutableLiveData<Boolean>()
    private val isUPKUpdateEnabled = MutableLiveData<Boolean>()
    private val isKoAPUpdateEnabled = MutableLiveData<Boolean>()
    private val isPIKoAPUpdateEnabled = MutableLiveData<Boolean>()

    var isCheckUpdateButtonEnabled = MutableLiveData<Boolean>()

    init {
        isCheckUpdateButtonEnabled.value = true
        updateIsUpdateEnabled()
    }

    fun isUpdateEnabled(codex: Codex): MutableLiveData<Boolean>{
        return when(codex){
            Codex.UK -> isUKUpdateEnabled
            Codex.UPK -> isUPKUpdateEnabled
            Codex.KoAP -> isKoAPUpdateEnabled
            Codex.PIKoAP -> isPIKoAPUpdateEnabled
        }
    }

    fun updateIsUpdateEnabled(){
        isUKUpdateEnabled.value = CodexVersionParser.isHaveChanges(Codex.UK)
        isUPKUpdateEnabled.value = CodexVersionParser.isHaveChanges(Codex.UPK)
        isKoAPUpdateEnabled.value = CodexVersionParser.isHaveChanges(Codex.KoAP)
        isPIKoAPUpdateEnabled.value = CodexVersionParser.isHaveChanges(Codex.PIKoAP)
    }
}