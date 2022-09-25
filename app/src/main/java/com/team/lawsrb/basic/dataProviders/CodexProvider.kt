package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import com.team.lawsrb.basic.htmlParser.Codex
import com.team.lawsrb.basic.roomDatabase.CodexDatabase

/**
 * [CodexProvider] is an interface which give us access to a certain codex and its
 * [CodexDatabase], items for each ui page and some useful properties.
 *
 * @see BaseCodexProvider
 * @see CodexDatabase
 */
interface CodexProvider{

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

    //fun getCodexType(): Codex

    val isSectionPageItemsEmpty: Boolean
    val isChapterPageItemsEmpty: Boolean
    val isArticlePageItemsEmpty: Boolean

    val isSectionPageItemsNotEmpty: Boolean
    val isChapterPageItemsNotEmpty: Boolean
    val isArticlePageItemsNotEmpty: Boolean

}