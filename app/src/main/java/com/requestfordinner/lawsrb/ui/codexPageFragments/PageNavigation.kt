package com.requestfordinner.lawsrb.ui.codexPageFragments

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider
import java.util.*
import kotlin.concurrent.schedule

/**
 * [PageNavigation] is a *object* class which allow page fragments (**Article page**,
 * **Chapter page** and **Section page**) to switch between other page fragments.
 *
 * To use [PageNavigation] it need:
 *  1. Set up [viewPager] with codex fragment (**CodexUKFragment** and others) [ViewPager2].
 *  2. Use [addRecyclerWithItems] to add each page fragment [RecyclerView].
 */
object PageNavigation {
    private const val DELAY_BEFORE_SCROLLING = 300L

    /** [viewPager] is a currently open codex page [ViewPager2]. */
    var viewPager: ViewPager2? = null

    /** [recyclerWithItemsMap] contains [RecyclerWithItems] of each ui page. */
    private var recyclerWithItemsMap = mutableMapOf<Page, RecyclerWithItems>()

    /** [RecyclerWithItems] is a class which represents single [RecyclerView] and its items. */
    private class RecyclerWithItems(val recycler: RecyclerView, val items: List<Any>)

    /** Return current [Page] from [viewPager]. */
    val currentPage: Page
        get() {
            return when (viewPager!!.currentItem) {
                0 -> Page.SECTIONS
                1 -> Page.CHAPTERS
                2 -> Page.ARTICLES
                else -> throw IllegalArgumentException("viewPager item was ${viewPager!!.currentItem}, should be in [0..2]")
            }
        }

    /** [Page] is an `enum` class which represents codex ui pages. */
    enum class Page(val itemIndex: Int) {
        SECTIONS(0),
        CHAPTERS(1),
        ARTICLES(2)
    }

    /** This method set up given [page] with given [recycler] and [items]. */
    fun addRecyclerWithItems(recycler: RecyclerView, items: List<Any>, page: Page) {
        recyclerWithItemsMap[page] = RecyclerWithItems(recycler, items)
    }

    /** This method smooth move to given [page]. */
    fun moveTo(page: Page) {
        viewPager?.setCurrentItem(page.itemIndex, true)
    }

    /**
     * This method opens the page to the left of current one
     * and smooth scroll it to [codexObject].
     */
    fun moveLeftTo(codexObject: Any) {
        viewPager?.setCurrentItem(currentPage.itemIndex - 1, true)
        Timer().schedule(DELAY_BEFORE_SCROLLING) {
            scrollPageTo(codexObject)
        }
    }

    /**
     * This method opens the page to the right of current one
     * and smooth scroll it to [codexObject].
     */
    fun moveRightTo(codexObject: Any) {
        viewPager?.setCurrentItem(currentPage.itemIndex + 1, true)
        Timer().schedule(DELAY_BEFORE_SCROLLING) {
            scrollPageTo(codexObject)
        }
    }

    /** This method smooth scroll current page to [codexObject]. */
    private fun scrollPageTo(codexObject: Any) {
        recyclerWithItemsMap[currentPage]?.let { recycler ->
            val position = recycler.items.indexOfFirst { it == codexObject }
            if (position != -1) {
                recycler.recycler.smoothScrollToPosition(position)
            }
        }
    }

    /**
     * This method adjust current page by page items from [codeProvider].
     *
     * **Rules**
     *  1. if current page is not empty or all pages are empty, then stay on this page.
     *  2. if only one page is not empty, then move to that page.
     *  3. if current page is empty, but two others are not, then move to nearest (**Article page**
     *  in priority).
     */
    fun adjustCurrentPageByItems(codeProvider: CodexProvider) {
        /*
            s - section, c - chapter, a - Article (S/C/A - current page)
            0 - empty page
            1 - not empty page

              All variations:
            S|c|a  s|C|a  s|c|A
            0|0|0  0|0|0  0|0|0
            0|0|1  0|0|1  0|0|1
            0|1|0  0|1|0  0|1|0
            0|1|1  0|1|1  0|1|1
            1|0|0  1|0|0  1|0|0
            1|0|1  1|0|1  1|0|1
            1|1|0  1|1|0  1|1|0
            1|1|1  1|1|1  1|1|1
         */


        /* (1) if current page is not empty, then stay on this page
                After:
            S|c|a  s|C|a  s|c|A
            0|0|0  0|0|0  0|0|0
            0|0|1  0|0|1  0|1|0
            0|1|0  1|0|0  1|0|0
            0|1|1  1|0|1  1|1|0
         */
        if (currentPage == Page.SECTIONS &&
            codeProvider.isSectionPageItemsNotEmpty ||
            currentPage == Page.CHAPTERS &&
            codeProvider.isChapterPageItemsNotEmpty ||
            currentPage == Page.ARTICLES &&
            codeProvider.isArticlePageItemsNotEmpty
        ) {
            return
        }


        /* (2) if all pages are empty, then stay on current page
                  After:
            S|c|a  s|C|a  s|c|A
            0|0|1  0|0|1  0|1|0
            0|1|0  1|0|0  1|0|0
            0|1|1  1|0|1  1|1|0
         */
        if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsEmpty
        ) {
            return
        }


        /* (3) if only one page is not empty, then choose that page
                  After:
            S|c|a  s|C|a  s|c|A
            0|1|1  1|0|1  1|1|0
         */
        if (codeProvider.isSectionPageItemsNotEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsEmpty
        ) {
            moveTo(Page.SECTIONS)
        } else if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsNotEmpty &&
            codeProvider.isArticlePageItemsEmpty
        ) {
            moveTo(Page.CHAPTERS)
        } else if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsNotEmpty
        ) {
            moveTo(Page.ARTICLES)
        }

        // (4) solve last variations
        else if (currentPage == Page.SECTIONS) {
            moveTo(Page.CHAPTERS)
        } else if (currentPage == Page.CHAPTERS) {
            moveTo(Page.ARTICLES)
        } else if (currentPage == Page.ARTICLES) {
            moveTo(Page.CHAPTERS)
        }
    }

    /** This method clear [viewPager], [RecyclerView]s and page items. */
    fun clear() {
        viewPager = null
        recyclerWithItemsMap.clear()
    }
}
