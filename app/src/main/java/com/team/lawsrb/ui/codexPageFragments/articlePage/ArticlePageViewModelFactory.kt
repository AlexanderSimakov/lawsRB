package com.team.lawsrb.ui.codexPageFragments.articlePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ArticlePageViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticlePageViewModel(codexProvider) as T
    }
}
