package com.requestfordinner.lawsrb.ui.codexFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.databinding.FragmentUpkBinding
import com.requestfordinner.lawsrb.ui.CollectionAdapter
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation

/**
 * [CodexUPKFragment] is a child of [Fragment] that represents UPK codex page with
 * **Section**, **Chapter** and **Article** pages using [ViewPager2].
 *
 * @see Fragment
 * @see ViewPager2
 */
class CodexUPKFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    private var _binding: FragmentUpkBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.upkPager
        viewPager.adapter = CollectionAdapter(BaseCodexProvider.UPK, this)
        viewPager.offscreenPageLimit = 2
        PageNavigation.clear()
        PageNavigation.viewPager = viewPager

        val tabLayout = binding.upkTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.pager_item_sections)
                1 -> getString(R.string.pager_item_chapters)
                2 -> getString(R.string.pager_item_articles)
                else -> throw IllegalArgumentException("Position was $position")
            }
        }.attach()
    }
}
