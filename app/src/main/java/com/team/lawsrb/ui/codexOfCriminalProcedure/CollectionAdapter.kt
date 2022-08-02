package com.team.lawsrb.ui.codexOfCriminalProcedure

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexOfCriminalProcedureProvider
import com.team.lawsrb.ui.codexObjectFragments.ArticleObjectFragment
import com.team.lawsrb.ui.codexObjectFragments.ChapterObjectFragment
import com.team.lawsrb.ui.codexObjectFragments.SectionObjectFragment

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionObjectFragment(CodexOfCriminalProcedureProvider, R.id.code_of_criminal_procedure_pager)
            1 -> ChapterObjectFragment(CodexOfCriminalProcedureProvider, R.id.code_of_criminal_procedure_pager)
            2 -> ArticleObjectFragment(CodexOfCriminalProcedureProvider, R.id.code_of_criminal_procedure_pager)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
