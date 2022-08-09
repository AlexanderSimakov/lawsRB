package com.team.lawsrb.ui.codexKoAP

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.lawsrb.basic.dataProviders.CodexOfCriminalProcedureProvider
import com.team.lawsrb.ui.codexPageFragments.articlePage.ArticlePageFragment
import com.team.lawsrb.ui.codexPageFragments.chapterPage.ChapterPageFragment
import com.team.lawsrb.ui.codexPageFragments.sectionPage.SectionPageFragment

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        // TODO: Add actual provider
        return when (position){
            0 -> SectionPageFragment(CodexOfCriminalProcedureProvider)
            1 -> ChapterPageFragment(CodexOfCriminalProcedureProvider)
            2 -> ArticlePageFragment(CodexOfCriminalProcedureProvider)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
