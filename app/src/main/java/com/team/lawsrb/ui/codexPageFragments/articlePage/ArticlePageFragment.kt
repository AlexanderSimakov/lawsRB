package com.team.lawsrb.ui.codexPageFragments.articlePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ArticlePageFragment(private val codeProvider: CodexProvider) : Fragment() {
    private lateinit var model: ArticlePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        model = ViewModelProvider(this, ArticleViewModelFactory(codeProvider))
            .get(ArticlePageViewModel::class.java)

        return inflater.inflate(R.layout.fragment_code_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = model.getItems().value as List<Any>

        val rvItems = view.findViewById<View>(R.id.code_viewer_fragment_recycler_view) as RecyclerView
        rvItems.adapter = ArticlePageAdapter(items, rvItems, codeProvider.database.articlesDao())
        rvItems.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerView(rvItems, items, 2)

        val itemsObserver = Observer<List<Any>> { newItems ->
            rvItems.adapter = ArticlePageAdapter(newItems, rvItems, codeProvider.database.articlesDao())
            PageNavigation.addRecyclerView(rvItems, newItems, 2)
        }

        model.getItems().observe(viewLifecycleOwner, itemsObserver)
    }
}
