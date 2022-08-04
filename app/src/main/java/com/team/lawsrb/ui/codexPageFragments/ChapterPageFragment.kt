package com.team.lawsrb.ui.codexPageFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.lawsrb.R
import com.team.lawsrb.basic.codexObjects.Chapter
import com.team.lawsrb.basic.codexObjects.Section
import com.team.lawsrb.basic.dataProviders.CodexProvider

class ChapterPageFragment(private val codeProvider: CodexProvider,
                          private val pager_id: Int) : Fragment() {
    //private lateinit var layout: LinearLayout
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
        rvItems.adapter = ChapterPageAdapter(items)
        rvItems.layoutManager = LinearLayoutManager(context)

        /*
        Scrollable.view = view
        layout = view.findViewById<LinearLayout>(R.id.code_viewer_fragment_content)

        for (section in codeProvider.getSections()){
            addSectionToLayout(section)
            codeProvider.getChapters(section).forEach { addChapterToLayout(it) }
        }
        */
    }

    private fun initItems(){
        for (section in codeProvider.getSections()){
            items.add(section)
            codeProvider.getChapters(section).forEach { items.add(it) }
        }
    }

    /*
    private fun addSectionToLayout(section: Section){
        val sectionCard = CardViewFactory.getLightCard(layout.context, section.title, "Section content")
        sectionCard.tag = "Section${section.id}"
        sectionCard.setOnClickListener {
            val viewPager = it.rootView.findViewById<ViewPager2>(pager_id)
            viewPager.setCurrentItem(0, true)
            SectionPageFragment.scrollTo(sectionCard.tag.toString())
        }
        layout.addView(sectionCard)
    }

    private fun addChapterToLayout(chapter: Chapter){
        val chapterCard = CardViewFactory.getDarkCard(layout.context, chapter.title, "Chapter content")
        chapterCard.tag = "Chapter${chapter.id}"
        chapterCard.setOnClickListener {
            val viewPager = it.rootView.findViewById<ViewPager2>(pager_id)
            viewPager.setCurrentItem(2, true)
            ArticlePageFragment.scrollTo(chapterCard.tag.toString())
        }
        layout.addView(chapterCard)
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

class ChapterPageAdapter (private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isChapter = 2

    inner class SectionViewHolder(sectionCardView: View) : RecyclerView.ViewHolder(sectionCardView) {
        val title: TextView = sectionCardView.findViewById(R.id.light_card_title)
        val content: TextView = sectionCardView.findViewById(R.id.light_card_content)
    }

    inner class ChapterViewHolder(chapterCardView: View) : RecyclerView.ViewHolder(chapterCardView) {
        val title: TextView = chapterCardView.findViewById(R.id.dark_card_title)
        val content: TextView = chapterCardView.findViewById(R.id.dark_card_content)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]::class){
            Section::class -> isSection
            Chapter::class -> isChapter
            else -> throw IllegalArgumentException("items: List<Any> contain class ${items[position]::class.simpleName}, expected Section or Chapter")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            isSection -> {
                val lightCardView = inflater.inflate(R.layout.light_card, parent, false)
                SectionViewHolder(lightCardView)
            }
            isChapter -> {
                val lightCardView = inflater.inflate(R.layout.dark_card, parent, false)
                ChapterViewHolder(lightCardView)
            }
            else -> throw IllegalArgumentException("viewType was $viewType, expected $isSection or $isChapter")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        when (viewHolder.itemViewType){
            isSection -> {
                val section: Section = items[position] as Section
                (viewHolder as SectionViewHolder).title.text = section.title
                viewHolder.content.text = "Section content"
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                (viewHolder as ChapterViewHolder).title.text = chapter.title
                viewHolder.content.text = "Part content"
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isChapter")
        }
}
