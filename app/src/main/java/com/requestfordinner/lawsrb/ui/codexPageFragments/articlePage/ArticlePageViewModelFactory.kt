package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider

/**
 * [ArticlePageViewModelFactory] is a custom [ViewModelProvider.Factory] which main purpose is
 * to sent [codexProvider] to the [ArticlePageViewModel].
 *
 * @see ArticlePageViewModel
 */
class ArticlePageViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticlePageViewModel(codexProvider) as T
    }
}
