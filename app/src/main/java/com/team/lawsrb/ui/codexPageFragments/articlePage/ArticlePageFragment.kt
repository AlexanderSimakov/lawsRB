package com.team.lawsrb.ui.codexPageFragments.articlePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.databinding.FragmentCodexViewerBinding
import com.team.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

/**
 * [ArticlePageFragment] is a child of [Fragment] which represent **Article page** of any codex.
 * It use [codeProvider] to create ui list of elements which consist of Articles and Chapters.
 */
class ArticlePageFragment(private val codeProvider: CodexProvider) : Fragment() {

    constructor() : this(BaseCodexProvider.UK)

    private lateinit var model: ArticlePageViewModel
    private var _binding: FragmentCodexViewerBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        model = ViewModelProvider(this, ArticlePageViewModelFactory(codeProvider))
            .get(ArticlePageViewModel::class.java)

        _binding = FragmentCodexViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = binding.codexFragmentRecyclerView
        val pageItems = model.pageItems.value as List<Any>

        recycler.adapter = ArticlePageAdapter(pageItems, recycler, codeProvider.database.articlesDao())
        recycler.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerWithItems(recycler, pageItems, PageNavigation.Page.ARTICLES)

        // if the result of search or show favorites is empty, then show corresponding message.
        // else update recycler adapter with new items.
        model.pageItems.observe(viewLifecycleOwner) { newPageItems ->
            if (newPageItems.isEmpty()){
                binding.emptyMessage.visibility = View.VISIBLE
                if (BaseCodexProvider.search.isEmpty()){
                    binding.emptyMessage.text = resources.getString(R.string.empty_favorites)
                }else{
                    binding.emptyMessage.text = resources.getString(R.string.empty_search_message)
                }
                PageNavigation.adjustCurrentPageByItems(codeProvider)
            }else{
                binding.emptyMessage.visibility = View.GONE
                recycler.adapter = ArticlePageAdapter(newPageItems, recycler, codeProvider.database.articlesDao())
                PageNavigation.addRecyclerWithItems(recycler, newPageItems, PageNavigation.Page.ARTICLES)
            }
        }
    }

    override fun onStart(){
        super.onStart()

        // scroll down - hide fab
        // scroll up   - show fab
        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab)
        binding.codexFragmentRecyclerView
            .addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
