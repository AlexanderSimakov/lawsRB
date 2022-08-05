package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData

/**
 * Description
 */
interface CodexProvider{
    fun getSectionPageItems(): LiveData<List<Any>>
    fun getChapterPageItems(): LiveData<List<Any>>
    fun getArticlePageItems(): LiveData<List<Any>>
}