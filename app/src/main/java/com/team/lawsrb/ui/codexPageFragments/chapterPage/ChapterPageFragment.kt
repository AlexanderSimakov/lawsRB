package com.team.lawsrb.ui.codexPageFragments.chapterPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.databinding.FragmentCodexViewerBinding
import com.team.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ChapterPageFragment(private val codeProvider: CodexProvider) : Fragment() {

    constructor() : this(BaseCodexProvider.UK)

    private lateinit var model: ChapterPageViewModel
    private var _binding: FragmentCodexViewerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        model = ViewModelProvider(this, ChapterViewModelFactory(codeProvider))
            .get(ChapterPageViewModel::class.java)

        _binding = FragmentCodexViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = model.getItems().value as List<Any>

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

        val rvItems = binding.codexFragmentRecyclerView
        rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !fab.isShown){ // scroll up
                    fab.show()
                }else if (dy > 0 && fab.isShown){ // scroll down
                    fab.hide()
                }
            }
        })

        rvItems.adapter = ChapterPageAdapter(items)
        rvItems.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerView(rvItems, items, 1)

        val itemsObserver = Observer<List<Any>> { newItems ->
            rvItems.adapter = ChapterPageAdapter(newItems)
            PageNavigation.addRecyclerView(rvItems, newItems, 1)
        }

        model.getItems().observe(viewLifecycleOwner, itemsObserver)
    }
}
