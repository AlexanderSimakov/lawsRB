package com.team.lawsrb.ui.codeObjectFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodeProvider
import com.team.lawsrb.basic.dataProviders.CriminalCodeProvider
import com.team.lawsrb.ui.informationViewers.PartViewer
import com.team.lawsrb.ui.informationViewers.SectionViewer

class SectionObjectFragment(private val codeProvider: CodeProvider) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_code_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout = view.findViewById<LinearLayout>(R.id.code_viewer_fragment_content)
        for (part in codeProvider.getParts()){
            layout.addView(PartViewer(layout.context, part, false))

            for (section in codeProvider.getSections(part)){
                val sectionViewer = SectionViewer(layout.context, section)
                sectionViewer.setOnClickListener { view ->
                    val viewPager = view.rootView.findViewById<ViewPager2>(R.id.criminal_code_pager)
                    viewPager.setCurrentItem(1, true)
                }
                layout.addView(sectionViewer)
            }
        }
    }
}
