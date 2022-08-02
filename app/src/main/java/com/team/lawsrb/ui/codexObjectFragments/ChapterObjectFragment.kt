package com.team.lawsrb.ui.codexObjectFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.informationViewers.ChapterViewer
import com.team.lawsrb.ui.informationViewers.SectionViewer

class ChapterObjectFragment(private val codexProvider: CodexProvider, private val pager_id: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_code_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Scrollable.view = view
        val layout = view.findViewById<LinearLayout>(R.id.code_viewer_fragment_content)
        for (section in codexProvider.getSections()){
            val sectionViewer = SectionViewer(layout.context, section, false)
            sectionViewer.setOnClickListener { view ->
                val viewPager = view.rootView.findViewById<ViewPager2>(pager_id)
                viewPager.setCurrentItem(0, true)
                SectionObjectFragment.scrollTo(sectionViewer.tag.toString())
            }
            layout.addView(sectionViewer)

            for (chapter in codexProvider.getChapters(section)){
                val chapterView = ChapterViewer(layout.context, chapter)
                chapterView.setOnClickListener { view ->
                    val viewPager = view.rootView.findViewById<ViewPager2>(pager_id)
                    viewPager.setCurrentItem(2, true)
                    ArticleObjectFragment.scrollTo(chapterView.tag.toString())
                }
                layout.addView(chapterView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<ScrollView>(R.id.code_viewer_fragment_scroll_view)?.smoothScrollTo(0, y)
    }

    override fun onPause() {
        super.onPause()
        y = view?.findViewById<ScrollView>(R.id.code_viewer_fragment_scroll_view)?.scrollY ?: 0
    }

    companion object Scrollable{
        @SuppressLint("StaticFieldLeak")
        private var view: View? = null
        private var y: Int = 0

        fun scrollTo(tag: String){
            y = view?.findViewWithTag<View>(tag)?.top ?: y
        }
    }
}