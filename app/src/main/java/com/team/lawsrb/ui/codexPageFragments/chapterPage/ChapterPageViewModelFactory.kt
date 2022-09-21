package com.team.lawsrb.ui.codexPageFragments.chapterPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ChapterPageViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChapterPageViewModel(codexProvider) as T
    }
}
