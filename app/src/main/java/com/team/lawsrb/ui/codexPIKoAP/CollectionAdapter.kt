package com.team.lawsrb.ui.codexPIKoAP

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.lawsrb.basic.dataProviders.PIKoAPProvider
import com.team.lawsrb.ui.codexPageFragments.articlePage.ArticlePageFragment
import com.team.lawsrb.ui.codexPageFragments.chapterPage.ChapterPageFragment
import com.team.lawsrb.ui.codexPageFragments.sectionPage.SectionPageFragment

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionPageFragment(PIKoAPProvider)
            1 -> ChapterPageFragment(PIKoAPProvider)
            2 -> ArticlePageFragment(PIKoAPProvider)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
