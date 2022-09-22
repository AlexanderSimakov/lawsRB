package com.team.lawsrb.ui.codexPageFragments.sectionPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.team.lawsrb.basic.dataProviders.CodexProvider

/**
 * [SectionPageViewModel] is a child class of the [ViewModel] which is used in [SectionPageFragment].
 *
 * @param codexProvider Current codex provider.
 * @property pageItems Items which will shown on the **Section page**.
 * @see ViewModel
 * @see SectionPageViewModelFactory
 * @see SectionPageFragment
 */
class SectionPageViewModel(codexProvider: CodexProvider) : ViewModel(){
    val pageItems: LiveData<List<Any>> = codexProvider.getSectionPageItems()
}
