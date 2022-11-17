package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.roomDatabase.BaseCodexDatabase
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.requestfordinner.lawsrb.ui.FragmentNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ArticlePageViewModel] is a child class of the [ViewModel] which is used in [ArticlePageFragment].
 *
 * @param codexProvider Current codex provider.
 * @property pageItems Items which will shown on the **Article page**.
 * @see ViewModel
 * @see ArticlePageFragment
 */
class ArticlePageViewModel : ViewModel() {
    lateinit var pageItems: LiveData<List<Any>>

    private lateinit var articlesDao: ArticlesDao

    private var openedCodex: Codex = Codex.UK

    /** Updates [pageItems] by current page. Call this after getting [ArticlePageViewModel]. */
    fun update(activity: FragmentActivity) {
        openedCodex = FragmentNavigation(activity).getOpenedCode()
        pageItems = BaseCodexProvider.get(openedCodex).getArticlePageItems()
        articlesDao = BaseCodexProvider.get(openedCodex).database.articlesDao()
    }

    fun notifyOnArticleOpen(article: Article) {
        openedCodeArticles[openedCodex]?.add(article.id)
    }

    fun notifyOnArticleClose(article: Article) {
        openedCodeArticles[openedCodex]?.remove(article.id)
    }

    fun addToFavorites(article: Article) {
        changeIsLikedState(article, true)
    }

    fun removeFromFavorites(article: Article) {
        changeIsLikedState(article, false)
    }

    fun isOpened(article: Article): Boolean {
        return article.id in openedCodeArticles[openedCodex]!!
    }

    private fun changeIsLikedState(article: Article, isLiked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            articlesDao.update(article.copy(isLiked = isLiked))
        }
    }

    companion object {
        private val openedCodeArticles = mutableMapOf(
            Codex.UK to mutableSetOf<Int>(),
            Codex.UPK to mutableSetOf<Int>(),
            Codex.KoAP to mutableSetOf<Int>(),
            Codex.PIKoAP to mutableSetOf<Int>()
        )
    }
}

