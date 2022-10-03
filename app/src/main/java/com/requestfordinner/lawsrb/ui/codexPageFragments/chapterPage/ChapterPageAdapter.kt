package com.requestfordinner.lawsrb.ui.codexPageFragments.chapterPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation

/**
 * [ChapterPageAdapter] is a child of [RecyclerView.Adapter] which is used for creating
 * [Chapter] and [Section]'s views for **Chapter page**.
 *
 * @param items The list of all shown codex elements.
 *
 * @see RecyclerView
 * @see RecyclerView.Adapter
 */
class ChapterPageAdapter(private val items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isChapter = 2

    /**
     * [SectionViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Section card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
    inner class SectionViewHolder(sectionCardView: View) :
        RecyclerView.ViewHolder(sectionCardView) {
        val card: MaterialCardView = sectionCardView.findViewById(R.id.card)
        val title: TextView = sectionCardView.findViewById(R.id.title)
    }

    /**
     * [ChapterViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Chapter card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
    inner class ChapterViewHolder(chapterCardView: View) :
        RecyclerView.ViewHolder(chapterCardView) {
        val card: MaterialCardView = chapterCardView.findViewById(R.id.card)
        val title: TextView = chapterCardView.findViewById(R.id.title)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]::class) {
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
                val lightCardView = inflater.inflate(R.layout.title_card, parent, false)
                SectionViewHolder(lightCardView)
            }
            isChapter -> {
                val lightCardView = inflater.inflate(R.layout.item_card, parent, false)
                ChapterViewHolder(lightCardView)
            }
            else -> throw IllegalArgumentException("viewType was $viewType, expected $isSection or $isChapter")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        when (viewHolder.itemViewType) {
            isSection -> {
                val section: Section = items[position] as Section
                setUpSectionView(section, viewHolder)
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                setUpChapterView(chapter, viewHolder)
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isChapter")
        }

    /** This method set up given Section [viewHolder] with given [section]. */
    private fun setUpSectionView(section: Section, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as SectionViewHolder).title.text = section.title
        viewHolder.card.setOnClickListener {
            PageNavigation.moveLeftTo(section)
        }

        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
    }

    /** This method set up given Chapter [viewHolder] with given [chapter]. */
    private fun setUpChapterView(chapter: Chapter, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ChapterViewHolder).title.text = chapter.title
        viewHolder.card.setOnClickListener {
            PageNavigation.moveRightTo(chapter)
        }

        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
    }
}
