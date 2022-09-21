package com.team.lawsrb.ui.codexPageFragments.sectionPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider

/**
 * [SectionPageViewModelFactory] is a custom [ViewModelProvider.Factory] which main purpose is
 * to sent [codexProvider] to the [SectionPageViewModel].
 */
class SectionPageViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SectionPageViewModel(codexProvider) as T
    }
}
