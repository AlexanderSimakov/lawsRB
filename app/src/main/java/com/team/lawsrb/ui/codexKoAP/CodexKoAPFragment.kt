package com.team.lawsrb.ui.codexKoAP

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.databinding.FragmentKoapBinding
import com.team.lawsrb.ui.CollectionAdapter
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class CodexKoAPFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    private var _binding: FragmentKoapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentKoapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.koapPager
        viewPager.adapter = CollectionAdapter(BaseCodexProvider.KoAP, this)
        viewPager.offscreenPageLimit = 2
        PageNavigation.clear()
        PageNavigation.viewPager = viewPager

        val tabLayout = binding.koapTabLayout
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
