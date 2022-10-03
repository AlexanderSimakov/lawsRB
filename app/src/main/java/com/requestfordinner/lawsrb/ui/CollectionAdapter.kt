package com.requestfordinner.lawsrb.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage.ArticlePageFragment
import com.requestfordinner.lawsrb.ui.codexPageFragments.chapterPage.ChapterPageFragment
import com.requestfordinner.lawsrb.ui.codexPageFragments.sectionPage.SectionPageFragment
import androidx.viewpager2.widget.ViewPager2

/**
 * [CollectionAdapter] is a child of [FragmentStateAdapter] which is used as adapter in [ViewPager2]
 * to create codex [SectionPageFragment], [ChapterPageFragment], [ArticlePageFragment].
 *
 * @see FragmentStateAdapter
 * @see ViewPager2
 * @see SectionPageFragment
 * @see ChapterPageFragment
 * @see ArticlePageFragment
 */
class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val itemCount: Int = 3

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SectionPageFragment()
            1 -> ChapterPageFragment()
            2 -> ArticlePageFragment()
            else -> throw IllegalArgumentException("Position was $position, expected from 0 to ${itemCount - 1}")
        }
    }
}
