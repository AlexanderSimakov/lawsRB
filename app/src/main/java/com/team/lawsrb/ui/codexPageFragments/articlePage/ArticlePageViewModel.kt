package com.team.lawsrb.ui.codexPageFragments.articlePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ArticlePageViewModel(codexProvider: CodexProvider) : ViewModel(){
    private val articleItems: LiveData<List<Any>> = codexProvider.getArticlePageItems()
    fun getItems() = articleItems
}

class ArticleViewModelFactory(private val codexProvider: CodexProvider) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticlePageViewModel(codexProvider) as T
    }
}
