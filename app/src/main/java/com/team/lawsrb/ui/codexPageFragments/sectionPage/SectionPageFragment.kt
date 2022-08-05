package com.team.lawsrb.ui.codexPageFragments.sectionPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.codexPageFragments.CenterLayoutManager
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class SectionPageFragment(private val codeProvider: CodexProvider,
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

        val rvSections = view.findViewById<View>(R.id.code_viewer_fragment_recycler_view) as RecyclerView
        rvSections.adapter = SectionPageAdapter(items)
        rvSections.layoutManager = context?.let { CenterLayoutManager(it) }
        PageNavigation.addRecyclerView(rvSections, items, 0)
    }

    private fun initItems(){
        for (part in codeProvider.getParts()){
            items.add(part)
            codeProvider.getSections(part).forEach { items.add(it) }
        }
    }
}
