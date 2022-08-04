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
import com.team.lawsrb.basic.codexObjects.Article
import com.team.lawsrb.basic.codexObjects.Chapter
import com.team.lawsrb.basic.dataProviders.CodexProvider
import com.team.lawsrb.ui.informationViewers.*

class ArticlePageFragment(private val codeProvider: CodexProvider,
                          private val pager_id: Int) : Fragment() {
    //private lateinit var layout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_code_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        Scrollable.view = view
        layout = view.findViewById(R.id.code_viewer_fragment_content)

        for (chapter in codeProvider.getChapters()){
            addChapterToLayout(chapter)
            codeProvider.getArticles(chapter).forEach { addArticleToLayout(it) }
        }
        */
    }

    /*
    private fun addChapterToLayout(chapter: Chapter){
        val chapterCard = CardViewFactory.getLightCard(layout.context, chapter.title, "Chapter content")
        chapterCard.tag = "Chapter${chapter.id}"
        chapterCard.setOnClickListener {
            val viewPager = it.rootView.findViewById<ViewPager2>(pager_id)
            viewPager.setCurrentItem(1, true)
            ChapterPageFragment.scrollTo(chapterCard.tag.toString())
        }
        layout.addView(chapterCard)
    }

    private fun addArticleToLayout(article: Article){
        val articleCard = CardViewFactory.getDarkCardWithButton(layout.context, article.title, "Article content")
        articleCard.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(((view as ViewGroup).parent as View).id, ArticleContentFragment(article))
                ?.addToBackStack(ArticleContentFragment::class.java.name)
                ?.commit()
        }
        layout.addView(articleCard)
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
     */
}