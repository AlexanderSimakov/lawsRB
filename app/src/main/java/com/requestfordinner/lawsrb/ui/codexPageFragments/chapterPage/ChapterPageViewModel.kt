package com.requestfordinner.lawsrb.ui.codexPageFragments.chapterPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider

/**
 * [ChapterPageViewModel] is a child class of the [ViewModel] which is used in [ChapterPageFragment].
 *
 * @param codexProvider Current codex provider.
 * @property pageItems Items which will shown on the **Chapter page**.
 * @see ViewModel
 * @see ChapterPageViewModelFactory
 * @see ChapterPageFragment
 */
class ChapterPageViewModel(codexProvider: CodexProvider) :ViewModel(){
    val pageItems: LiveData<List<Any>> = codexProvider.getChapterPageItems()
}
