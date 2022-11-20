package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.databinding.ArticleCardBinding
import com.requestfordinner.lawsrb.databinding.FragmentCodexViewerBinding
import com.requestfordinner.lawsrb.ui.FragmentNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation
import com.requestfordinner.lawsrb.ui.codexPageFragments.items.ArticleItem
import com.requestfordinner.lawsrb.ui.codexPageFragments.items.TitleCardItem
import com.requestfordinner.lawsrb.ui.util.movementAnimation
import com.xwray.groupie.GroupieAdapter

/**
 * [ArticlePageFragment] is a child of [Fragment] which represent **Article page** of any codex.
 */
class ArticlePageFragment : Fragment() {

    private lateinit var fragmentNav: FragmentNavigation
    private lateinit var codexProvider: CodexProvider
    private lateinit var model: ArticlePageViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentCodexViewerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentNav = FragmentNavigation(requireActivity())
        codexProvider = BaseCodexProvider.get(fragmentNav.getOpenedCode())

        binding = FragmentCodexViewerBinding.inflate(inflater, container, false)
        model = ViewModelProvider(this)[ArticlePageViewModel::class.java]
        model.update(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = binding.codexFragmentRecyclerView
        val pageItems = model.pageItems.value as List<Any>

        codexProvider = BaseCodexProvider.get(fragmentNav.getOpenedCode())

        recycler.adapter = getAdapter(pageItems)
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
                recycler.adapter = getAdapter(newPageItems)
                PageNavigation.addRecyclerWithItems(
                    recycler,
                    newPageItems,
                    PageNavigation.Page.ARTICLES
                )
            }
        }
    }

    /** Returns [GroupieAdapter] for given [items]. */
    private fun getAdapter(items: List<Any>): GroupieAdapter {
        val adapter = GroupieAdapter()
        items.forEach { item ->
            when (item) {
                is Chapter -> adapter.add(TitleCardItem(item) {
                    PageNavigation.moveLeftTo(it as Section)
                })
                is Article -> adapter.add(ArticleItem(item, model.isOpened(item),
                    { article, itemBinding -> onArticleCardClick(article, itemBinding) },
                    { article, isLiked -> onArticleCheckBoxClick(article, isLiked) }
                ))
            }
        }
        return adapter
    }

    private fun onArticleCardClick(article: Article, item: ArticleCardBinding) {
        TransitionManager.beginDelayedTransition(
            recycler as ViewGroup?,
            AutoTransition()
        )
        if (item.expandableLayout.visibility == View.VISIBLE) {
            item.expandableLayout.visibility = View.GONE
            model.notifyOnArticleClose(article)
        } else {
            item.expandableLayout.visibility = View.VISIBLE
            model.notifyOnArticleOpen(article)
        }
    }

    private fun onArticleCheckBoxClick(article: Article, isLiked: Boolean) {
        if (isLiked) model.addToFavorites(article)
        else model.removeFromFavorites(article)
    }

    override fun onStart() {
        super.onStart()

        // scroll down - hide fab
        // scroll up   - show fab
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        binding.codexFragmentRecyclerView
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    fab.apply {
                        if (dy < 0 && !isShown) { // scroll up
                            movementAnimation(requireContext(), View.VISIBLE)
                        } else if (dy > 0 && isShown) { // scroll down
                            movementAnimation(requireContext(), View.GONE)
                        }
                    }
                }
            })
    }
}
