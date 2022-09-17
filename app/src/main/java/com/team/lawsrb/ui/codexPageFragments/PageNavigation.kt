package com.team.lawsrb.ui.codexPageFragments

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.team.lawsrb.basic.dataProviders.CodexProvider
import java.util.*
import kotlin.concurrent.schedule

object PageNavigation {
    var viewPager: ViewPager2? = null

    private class Recycler(val recyclerView: RecyclerView, val items: List<Any>)
    private var recyclers: MutableList<Recycler> = mutableListOf()
    private val delayBeforeScrolling = 300L

    val currentPage: Page
        get() {
            return when(viewPager!!.currentItem){
                0 -> Page.SECTIONS
                1 -> Page.CHAPTERS
                2 -> Page.ARTICLES
                else -> throw IllegalArgumentException("viewPager item was ${viewPager!!.currentItem}, should be in [0..2]")
            }
        }

    enum class Page(val itemIndex: Int) {
        SECTIONS(0),
        CHAPTERS(1),
        ARTICLES(2)
    }

    fun addRecyclerView(view: RecyclerView, items: List<Any>, number: Int) {
        recyclers.add(number, Recycler(view, items))
    }

    fun moveTo(page: Page){
        viewPager!!.setCurrentItem(page.itemIndex, true)
    }


    fun moveLeftTo(codexObject: Any) {
        viewPager!!.setCurrentItem(viewPager!!.currentItem - 1, true)
        Timer().schedule(delayBeforeScrolling){
            scrollPageTo(codexObject)
        }
    }

    fun moveRightTo(codexObject: Any) {
        viewPager!!.setCurrentItem(viewPager!!.currentItem + 1, true)
        Timer().schedule(delayBeforeScrolling){
            scrollPageTo(codexObject)
        }
    }

    private fun scrollPageTo(codexObject: Any){
        recyclers[viewPager!!.currentItem].let { recycler ->
            val position = recycler.items.indexOfFirst { it == codexObject }
            recycler.recyclerView.smoothScrollToPosition(position)
        }
    }

    fun adjustCurrentPageByItems(codeProvider: CodexProvider){
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

            (1) if current page is not empty, then stay on this page
                  Before:
            S|c|a  s|C|a  s|c|A
            0|0|0  0|0|0  0|0|0
            0|0|1  0|0|1  0|1|0
            0|1|0  1|0|0  1|0|0
            0|1|1  1|0|1  1|1|0

            (2) if all pages are empty, then stay on current page
                  Before:
            S|c|a  s|C|a  s|c|A
            0|0|1  0|0|1  0|1|0
            0|1|0  1|0|0  1|0|0
            0|1|1  1|0|1  1|1|0

            (3) if only one page is not empty, then choose that page
                  Before:
            S|c|a  s|C|a  s|c|A
            0|1|1  1|0|1  1|1|0
         */

        // (1) if current page is not empty, then stay on this page
        if (currentPage == Page.SECTIONS &&
            codeProvider.isSectionPageItemsNotEmpty ||
            currentPage == Page.CHAPTERS &&
            codeProvider.isChapterPageItemsNotEmpty ||
            currentPage == Page.ARTICLES &&
            codeProvider.isArticlePageItemsNotEmpty){
            return
        }

        // (2) if all pages are empty, then stay on current page
        if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsEmpty){
            return
        }

        // (3) if only one page is not empty, then choose those page
        if (codeProvider.isSectionPageItemsNotEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsEmpty){
            moveTo(Page.SECTIONS)
        }
        else if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsNotEmpty &&
            codeProvider.isArticlePageItemsEmpty){
            moveTo(Page.CHAPTERS)
        }
        else if (codeProvider.isSectionPageItemsEmpty &&
            codeProvider.isChapterPageItemsEmpty &&
            codeProvider.isArticlePageItemsNotEmpty){
            moveTo(Page.ARTICLES)
        }

        // (4) solve last variations
        else if (currentPage == Page.SECTIONS){
            moveTo(Page.CHAPTERS)
        }
        else if (currentPage == Page.CHAPTERS){
            moveTo(Page.ARTICLES)
        }
        else if (currentPage == Page.ARTICLES){
            moveTo(Page.CHAPTERS)
        }
    }

    fun clear(){
        viewPager = null
        recyclers.clear()
    }
}