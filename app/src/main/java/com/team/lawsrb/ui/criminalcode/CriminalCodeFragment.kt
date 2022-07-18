package com.team.lawsrb.ui.criminalcode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.team.lawsrb.R
import com.team.lawsrb.ui.codeObjectFragments.ArticleObjectFragment
import com.team.lawsrb.ui.codeObjectFragments.ChapterObjectFragment
import com.team.lawsrb.ui.codeObjectFragments.SectionObjectFragment

class CriminalCodeFragment : Fragment() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_criminal_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager = view.findViewById(R.id.criminal_code_pager)
        viewPager.adapter = demoCollectionAdapter
        val tabLayout = view.findViewById<com.google.android.material.tabs.TabLayout>(R.id.criminal_code_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position){
                0 -> getString(R.string.pager_item_sections)
                1 -> getString(R.string.pager_item_chapters)
                else -> getString(R.string.pager_item_articles)
            }
        }.attach()
    }
}

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionObjectFragment()
            1 -> ChapterObjectFragment()
            else -> ArticleObjectFragment()
        }
    }
}