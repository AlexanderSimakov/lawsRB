package com.team.lawsrb.ui.criminalCodex

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CriminalCodexProvider
import com.team.lawsrb.ui.codexPageFragments.ArticlePageFragment
import com.team.lawsrb.ui.codexPageFragments.ChapterPageFragment
import com.team.lawsrb.ui.codexPageFragments.SectionPageFragment

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionPageFragment(CriminalCodexProvider, R.id.criminal_code_pager)
            1 -> ChapterPageFragment(CriminalCodexProvider, R.id.criminal_code_pager)
            2 -> ArticlePageFragment(CriminalCodexProvider, R.id.criminal_code_pager)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
