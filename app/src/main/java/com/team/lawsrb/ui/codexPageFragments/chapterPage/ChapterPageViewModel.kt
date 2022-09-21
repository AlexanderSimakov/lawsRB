package com.team.lawsrb.ui.codexPageFragments.chapterPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ChapterPageViewModel(codexProvider: CodexProvider) :ViewModel(){
    val pageItems: LiveData<List<Any>> = codexProvider.getChapterPageItems()
}
