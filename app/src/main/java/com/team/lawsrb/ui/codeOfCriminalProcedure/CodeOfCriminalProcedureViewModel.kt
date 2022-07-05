package com.team.lawsrb.ui.codeOfCriminalProcedure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CodeOfCriminalProcedureViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Code of Criminal Procedure Fragment"
    }
    val text: LiveData<String> = _text

}