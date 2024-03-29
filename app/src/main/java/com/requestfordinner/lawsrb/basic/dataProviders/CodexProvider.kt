package com.requestfordinner.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.roomDatabase.CodexDatabase

/**
 * [CodexProvider] is an interface which give us access to a certain codex and its
 * [CodexDatabase], items for each ui page and some useful properties.
 *
 * @see BaseCodexProvider
 * @see CodexDatabase
 */
interface CodexProvider {

    /** Provides access to simple [CodexDatabase] methods. */
    val database: CodexDatabase

    /** Storing code type from [Codex] */
    val codeType: Codex

    /**
     * This method return **Section Page** [LiveData] items.
     *
     * This items are displayed on the **Section Page**,
     * and consist of Parts and Sections.
     */
    fun getSectionPageItems(): LiveData<List<Any>>

    /**
     * This method return **Chapter Page** [LiveData] items.
     *
     * This items are displayed on the **Chapter Page**,
     * and consist of Sections and Chapters.
     */
    fun getChapterPageItems(): LiveData<List<Any>>

    /**
     * This method return **Article Page** [LiveData] items.
     *
     * This items are displayed on the **Article Page**,
     * and consist of Chapters and Articles.
     */
    fun getArticlePageItems(): LiveData<List<Any>>

    /** Return `true` if section page item list is empty. */
    val isSectionPageItemsEmpty: Boolean

    /** Return `true` if chapter page item list is empty. */
    val isChapterPageItemsEmpty: Boolean

    /** Return `true` if articles page item list is empty. */
    val isArticlePageItemsEmpty: Boolean

    /** Return `true` if section page item list is **not** empty. */
    val isSectionPageItemsNotEmpty: Boolean

    /** Return `true` if chapter page item list is **not** empty. */
    val isChapterPageItemsNotEmpty: Boolean

    /** Return `true` if article page item list is **not** empty. */
    val isArticlePageItemsNotEmpty: Boolean
}
