package com.team.lawsrb.ui.codexPageFragments.articlePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ArticlePageViewModel(codexProvider: CodexProvider) : ViewModel(){
    private val articleItems: LiveData<List<Any>> = codexProvider.getArticlePageItems()
    fun getItems() = articleItems
}

