package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import android.os.Bundle
import android.util.Log
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
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.databinding.FragmentCodexViewerBinding
import com.requestfordinner.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation

/**
 * [ArticlePageFragment] is a child of [Fragment] which represent **Article page** of any codex.
 * It use [codexProvider] to create ui list of elements which consist of Articles and Chapters.
 */
class ArticlePageFragment(codexProvider: CodexProvider) : Fragment() {

    /** This constructor is called then app theme changes. */
    constructor() : this(codexProvider)

    private val TAG = "ArticlePageFragment"

    private lateinit var model: ArticlePageViewModel
    private var _binding: FragmentCodexViewerBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    init {
        ArticlePageFragment.codexProvider = codexProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        model = ViewModelProvider(this, ArticlePageViewModelFactory(codexProvider))
            .get(ArticlePageViewModel::class.java)

        _binding = FragmentCodexViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = binding.codexFragmentRecyclerView
        val pageItems = model.pageItems.value as List<Any>

        recycler.adapter =
            ArticlePageAdapter(pageItems, recycler, codexProvider.database.articlesDao())
        recycler.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerWithItems(recycler, pageItems, PageNavigation.Page.ARTICLES)

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
                recycler.adapter =
                    ArticlePageAdapter(newPageItems, recycler, codexProvider.database.articlesDao())
                PageNavigation.addRecyclerWithItems(
                    recycler,
                    newPageItems,
                    PageNavigation.Page.ARTICLES
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        currentCodeType = getCurrentCodeType()

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

    private fun getCurrentFragment(): Fragment? {
        return try {
            val navHost =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            navHost?.let { navFragment ->
                navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                    Log.i(TAG, "The fragment is: $fragment")
                    return fragment
                }
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "Returnable value is null: ${e.message}")
            return null
        }
    }

    private fun getCurrentCodeType(): Codex {
        val containingAttribute: String = getCurrentFragment().toString()

        return if (containingAttribute.contains("UK")) Codex.UK
        else if (containingAttribute.contains("UPK")) Codex.UPK
        else if (containingAttribute.contains("KoAP")) Codex.KoAP
        else if (containingAttribute.contains("PIKoAP")) Codex.PIKoAP
        else throw Exception("The code type is not defined!")
    }

    companion object {
        lateinit var codexProvider: CodexProvider
        lateinit var currentCodeType: Codex
    }
}
