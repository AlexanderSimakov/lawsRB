package com.team.lawsrb.ui.codexPageFragments.chapterPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team.lawsrb.R
import com.team.lawsrb.basic.codexObjects.Chapter
import com.team.lawsrb.basic.codexObjects.Section
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ChapterPageAdapter (private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isChapter = 2

    inner class SectionViewHolder(sectionCardView: View) : RecyclerView.ViewHolder(sectionCardView) {
        val card: MaterialCardView = sectionCardView.findViewById(R.id.light_card)
        val title: TextView = sectionCardView.findViewById(R.id.light_card_title)
        val content: TextView = sectionCardView.findViewById(R.id.light_card_content)
    }

    inner class ChapterViewHolder(chapterCardView: View) : RecyclerView.ViewHolder(chapterCardView) {
        val card: MaterialCardView = chapterCardView.findViewById(R.id.dark_card)
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
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveLeftTo(section.id)
                }
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                (viewHolder as ChapterViewHolder).title.text = chapter.title
                viewHolder.content.text = "Chapter content"
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveRightTo(chapter.id)
                }
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isChapter")
        }
}
