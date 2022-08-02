package com.team.lawsrb.ui.codexPageFragments

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
import com.team.lawsrb.ui.informationViewers.CardViewFactory

class ChapterPageFragment(private val codeProvider: CodexProvider, private val pager_id: Int) : Fragment() {

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
        for (section in codeProvider.getSections()){
            val sectionCard = CardViewFactory.getLightCard(layout.context, section.title, "Section content")
            sectionCard.tag = "Section${section.id}"
            sectionCard.setOnClickListener { view ->
                val viewPager = view.rootView.findViewById<ViewPager2>(pager_id)
                viewPager.setCurrentItem(0, true)
                SectionPageFragment.scrollTo(sectionCard.tag.toString())
            }
            layout.addView(sectionCard)

            for (chapter in codeProvider.getChapters(section)){
                val chapterCard = CardViewFactory.getDarkCard(layout.context, chapter.title, "Chapter content")
                chapterCard.tag = "Chapter${chapter.id}"
                chapterCard.setOnClickListener { view ->
                    val viewPager = view.rootView.findViewById<ViewPager2>(pager_id)
                    viewPager.setCurrentItem(2, true)
                    ArticlePageFragment.scrollTo(chapterCard.tag.toString())
                }
                layout.addView(chapterCard)
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