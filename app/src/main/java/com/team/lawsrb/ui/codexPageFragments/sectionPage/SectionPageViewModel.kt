package com.team.lawsrb.ui.codexPageFragments.sectionPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.dataProviders.CodexProvider

class SectionPageViewModel(codexProvider: CodexProvider) : ViewModel(){
    val pageItems: LiveData<List<Any>> = codexProvider.getSectionPageItems()
}
