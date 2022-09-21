package com.team.lawsrb.ui.codexPageFragments.chapterPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider

/**
 * [ChapterPageViewModelFactory] is a custom [ViewModelProvider.Factory] which main purpose is
 * to sent [codexProvider] to the [ChapterPageViewModel].
 *
 * @see ChapterPageViewModel
 */
class ChapterPageViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChapterPageViewModel(codexProvider) as T
    }
}
