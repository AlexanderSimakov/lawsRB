package com.team.lawsrb.ui.codexPIKoAP

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.team.lawsrb.R
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class CodexPIKoAPFragment : Fragment() {
    private lateinit var collectionAdapter: CollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Change layout to actual
        return inflater.inflate(R.layout.fragment_codex_of_criminal_procedure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectionAdapter = CollectionAdapter(this)
        // TODO: Change viewPage id to actual
        viewPager = view.findViewById(R.id.code_of_criminal_procedure_pager)
        viewPager.adapter = collectionAdapter
        PageNavigation.clear()
        PageNavigation.viewPager = viewPager

        // TODO: Change tabLayout id to actual
        val tabLayout = view.findViewById<com.google.android.material.tabs.TabLayout>(R.id.code_of_criminal_procedure_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position){
                0 -> getString(R.string.pager_item_sections)
                1 -> getString(R.string.pager_item_chapters)
                2 -> getString(R.string.pager_item_articles)
                else -> throw IllegalArgumentException("Position was $position")
            }
        }.attach()
    }
}
