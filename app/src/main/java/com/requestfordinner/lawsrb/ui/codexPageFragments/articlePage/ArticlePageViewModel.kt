package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider

/**
 * [ArticlePageViewModel] is a child class of the [ViewModel] which is used in [ArticlePageFragment].
 *
 * @param codexProvider Current codex provider.
 * @property pageItems Items which will shown on the **Article page**.
 * @see ViewModel
 * @see ArticlePageViewModelFactory
 * @see ArticlePageFragment
 */
class ArticlePageViewModel(codexProvider: CodexProvider) : ViewModel(){
    val pageItems: LiveData<List<Any>> = codexProvider.getArticlePageItems()
}

