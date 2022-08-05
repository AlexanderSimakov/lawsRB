package com.team.lawsrb.ui.codexPageFragments.articlePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ArticlePageFragment(private val codeProvider: CodexProvider,
                          private val pager_id: Int) : Fragment() {
    private var items: MutableList<Any> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_code_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initItems()

        val rvItems = view.findViewById<View>(R.id.code_viewer_fragment_recycler_view) as RecyclerView
        rvItems.adapter = ArticlePageAdapter(items, rvItems)
        rvItems.layoutManager = LinearLayoutManager(context)
        PageNavigation.addRecyclerView(rvItems, items, 2)
    }

    private fun initItems(){
        for (chapter in codeProvider.getChapters()){
            items.add(chapter)
            codeProvider.getArticles(chapter).forEach { items.add(it) }
        }
    }
}
