package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import com.team.lawsrb.basic.roomDatabase.CodexDatabase

/**
 * Description
 */
interface CodexProvider{
    val database: CodexDatabase

    fun getSectionPageItems(): LiveData<List<Any>>
    fun getChapterPageItems(): LiveData<List<Any>>
    fun getArticlePageItems(): LiveData<List<Any>>
}