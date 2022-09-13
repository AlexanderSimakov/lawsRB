package com.team.lawsrb.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.ui.codexPageFragments.articlePage.ArticlePageFragment
import com.team.lawsrb.ui.codexPageFragments.chapterPage.ChapterPageFragment
import com.team.lawsrb.ui.codexPageFragments.sectionPage.SectionPageFragment

class CollectionAdapter(private val codexProvider: CodexProvider, fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionPageFragment(codexProvider)
            1 -> ChapterPageFragment(codexProvider)
            2 -> ArticlePageFragment(codexProvider)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
