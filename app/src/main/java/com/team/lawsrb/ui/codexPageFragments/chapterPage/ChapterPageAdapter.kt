package com.team.lawsrb.ui.codexPageFragments.chapterPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team.lawsrb.R
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ChapterPageAdapter (private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isChapter = 2

    inner class SectionViewHolder(sectionCardView: View) : RecyclerView.ViewHolder(sectionCardView) {
        val card: MaterialCardView = sectionCardView.findViewById(R.id.title_card)
        val title: TextView = sectionCardView.findViewById(R.id.title_card_title)
    }

    inner class ChapterViewHolder(chapterCardView: View) : RecyclerView.ViewHolder(chapterCardView) {
        val card: MaterialCardView = chapterCardView.findViewById(R.id.item_card)
        val title: TextView = chapterCardView.findViewById(R.id.item_card_title)
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
        when (viewHolder.itemViewType){
            isSection -> {
                val section: Section = items[position] as Section
                (viewHolder as SectionViewHolder).title.text = section.title
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveLeftTo(section)
                }
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                (viewHolder as ChapterViewHolder).title.text = chapter.title
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveRightTo(chapter)
                }
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isChapter")
        }
}
