package com.team.lawsrb.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.FavoritesProvider
import com.team.lawsrb.ui.codexObjectFragments.ArticleObjectFragment
import com.team.lawsrb.ui.codexObjectFragments.ChapterObjectFragment
import com.team.lawsrb.ui.codexObjectFragments.SectionObjectFragment

class FavoritesFragment : Fragment() {
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager = view.findViewById(R.id.favorites_pager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout = view.findViewById<com.google.android.material.tabs.TabLayout>(R.id.favorites_tab_layout)
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

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> SectionObjectFragment(FavoritesProvider, R.id.favorites_pager)
            1 -> ChapterObjectFragment(FavoritesProvider, R.id.favorites_pager)
            2 -> ArticleObjectFragment(FavoritesProvider, R.id.favorites_pager)
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
