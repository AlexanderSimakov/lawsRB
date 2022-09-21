package com.team.lawsrb.ui.codexPageFragments.chapterPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ChapterPageViewModel(codexProvider: CodexProvider) :ViewModel(){
    private val chapterItems: LiveData<List<Any>> = codexProvider.getChapterPageItems()
    fun getItems() = chapterItems
}
