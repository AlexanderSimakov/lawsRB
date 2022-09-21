package com.team.lawsrb.ui.codexPageFragments.chapterPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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
        val rvItems = binding.codexFragmentRecyclerView
        val items = model.getItems().value as List<Any>

        rvItems.adapter = ChapterPageAdapter(items)
        rvItems.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerWithItems(rvItems, items, PageNavigation.Page.CHAPTERS)

        val itemsObserver = Observer<List<Any>> { newItems ->
            if (newItems.isEmpty()){
                binding.emptyMessage.visibility = View.VISIBLE
                if (BaseCodexProvider.search.isEmpty()){
                    binding.emptyMessage.text = resources.getString(R.string.empty_favorites)
                }else{
                    binding.emptyMessage.text = resources.getString(R.string.empty_search_message)
                }
                PageNavigation.adjustCurrentPageByItems(codeProvider)
            }else{
                binding.emptyMessage.visibility = View.GONE
                rvItems.adapter = ChapterPageAdapter(newItems)
                PageNavigation.addRecyclerWithItems(rvItems, newItems, PageNavigation.Page.CHAPTERS)
            }
        }

        model.getItems().observe(viewLifecycleOwner, itemsObserver)
    }

    override fun onStart(){
        super.onStart()

        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab)
        binding.codexFragmentRecyclerView
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                fab?.apply {
                    if (dy < 0 && !isShown){ // scroll up
                        show()
                    }else if (dy > 0 && isShown){ // scroll down
                        hide()
                    }
                }
            }
        })

    }
}
