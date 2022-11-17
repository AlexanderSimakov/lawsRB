package com.requestfordinner.lawsrb.ui.codexPageFragments.sectionPage

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.ui.FragmentNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage.ArticlePageViewModel

/**
 * [SectionPageViewModel] is a child class of the [ViewModel] which is used in [SectionPageFragment].
 *
 * @param codexProvider Current codex provider.
 * @property pageItems Items which will shown on the **Section page**.
 * @see ViewModel
 * @see SectionPageFragment
 */
class SectionPageViewModel : ViewModel() {
    lateinit var pageItems: LiveData<List<Any>>

    private var openedCodex: Codex = Codex.UK

    /** Updates [pageItems] by current page. Call this after getting [ArticlePageViewModel]. */
    fun update(activity: FragmentActivity) {
        openedCodex = FragmentNavigation(activity).getOpenedCode()
        pageItems = BaseCodexProvider.get(openedCodex).getSectionPageItems()
    }
}
