package com.requestfordinner.lawsrb.ui.codexPageFragments.sectionPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.dataProviders.CodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.databinding.FragmentCodexViewerBinding
import com.requestfordinner.lawsrb.ui.FragmentNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.items.DefaultCardItem
import com.requestfordinner.lawsrb.ui.codexPageFragments.items.TitleCardItem
import com.xwray.groupie.GroupieAdapter

/**
 * [SectionPageFragment] is a child of [Fragment] which represent **Section page** of any codex.
 */
class SectionPageFragment : Fragment() {

    private lateinit var fragmentNav: FragmentNavigation
    private lateinit var codexProvider: CodexProvider
    private lateinit var model: SectionPageViewModel
    private lateinit var binding: FragmentCodexViewerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentNav = FragmentNavigation(requireActivity())
        codexProvider = BaseCodexProvider.get(fragmentNav.getOpenedCode())

        model = ViewModelProvider(this)[SectionPageViewModel::class.java]
        model.update(requireActivity())

        binding = FragmentCodexViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = binding.codexFragmentRecyclerView
        val pageItems = model.pageItems.value as List<Any>

        codexProvider = BaseCodexProvider.get(fragmentNav.getOpenedCode())

        recycler.adapter = getAdapter(pageItems)
        recycler.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerWithItems(recycler, pageItems, PageNavigation.Page.SECTIONS)

        // if the result of search or show favorites is empty, then show corresponding message.
        // else update recycler adapter with new items.
        model.pageItems.observe(viewLifecycleOwner) { newPageItems ->
            if (newPageItems.isEmpty()) {
                binding.emptyMessage.visibility = View.VISIBLE
                if (BaseCodexProvider.search.isEmpty() && !BaseCodexProvider.showFavorites) {
                    binding.emptyMessage.text = resources.getString(R.string.empty_page)
                } else if (BaseCodexProvider.search.isEmpty()) {
                    binding.emptyMessage.text = resources.getString(R.string.empty_favorites)
                } else {
                    binding.emptyMessage.text = resources.getString(R.string.empty_search_message)
                }
                PageNavigation.adjustCurrentPageByItems(codexProvider)
            } else {
                binding.emptyMessage.visibility = View.GONE
                recycler.adapter = getAdapter(newPageItems)
                PageNavigation.addRecyclerWithItems(
                    recycler,
                    newPageItems,
                    PageNavigation.Page.SECTIONS
                )
            }
        }
    }

    /** Returns [GroupieAdapter] for given [items]. */
    private fun getAdapter(items: List<Any>): GroupieAdapter {
        val adapter = GroupieAdapter()
        items.forEach { item ->
            when (item) {
                is Part -> adapter.add(TitleCardItem(item) { })
                is Section -> adapter.add(DefaultCardItem(item) {
                    PageNavigation.moveRightTo(it as Section)
                })
            }
        }
        return adapter
    }

    override fun onStart() {
        super.onStart()

        // scroll down - hide fab
        // scroll up   - show fab
        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab)
        binding.codexFragmentRecyclerView
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    fab?.apply {
                        if (dy < 0 && !isShown) { // scroll up
                            show()
                        } else if (dy > 0 && isShown) { // scroll down
                            hide()
                        }
                    }
                }
            })
    }
}
