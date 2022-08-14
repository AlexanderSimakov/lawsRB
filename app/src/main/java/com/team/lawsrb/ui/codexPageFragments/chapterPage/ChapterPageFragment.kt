package com.team.lawsrb.ui.codexPageFragments.chapterPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ChapterPageFragment(private val codeProvider: CodexProvider) : Fragment() {
    private lateinit var model: ChapterPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        model = ViewModelProvider(this, ChapterViewModelFactory(codeProvider))
            .get(ChapterPageViewModel::class.java)

        return inflater.inflate(R.layout.fragment_codex_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = model.getItems().value as List<Any>

        val rvItems = view.findViewById<View>(R.id.codex_fragment_recycler_view) as RecyclerView
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
