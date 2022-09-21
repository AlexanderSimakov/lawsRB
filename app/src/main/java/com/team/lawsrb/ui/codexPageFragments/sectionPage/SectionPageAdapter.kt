package com.team.lawsrb.ui.codexPageFragments.sectionPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import com.team.lawsrb.ui.codexPageFragments.Highlighter
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class SectionPageAdapter (private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isPart = 2

    inner class PartViewHolder(partCardView: View) : RecyclerView.ViewHolder(partCardView) {
        val title: TextView = partCardView.findViewById(R.id.title_card_title)
    }

    inner class SectionViewHolder(sectionCardView: View) : RecyclerView.ViewHolder(sectionCardView) {
        val card: MaterialCardView = sectionCardView.findViewById(R.id.item_card)
        val title: TextView = sectionCardView.findViewById(R.id.item_card_title)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]::class){
            Section::class -> isSection
            Part::class -> isPart
            else -> throw IllegalArgumentException("items: List<Any> contain class ${items[position]::class.simpleName}, expected Section or Part")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            isSection -> {
                val lightCardView = inflater.inflate(R.layout.item_card, parent, false)
                SectionViewHolder(lightCardView)
            }
            isPart -> {
                val lightCardView = inflater.inflate(R.layout.title_card, parent, false)
                PartViewHolder(lightCardView)
            }
            else -> throw IllegalArgumentException("viewType was $viewType, expected $isSection or $isPart")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        when (viewHolder.itemViewType){
            isSection -> {
                val section: Section = items[position] as Section
                (viewHolder as SectionViewHolder).title.text = section.title
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveRightTo(section)
                }

                Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
            }
            isPart -> {
                val part: Part = items[position] as Part
                (viewHolder as PartViewHolder).title.text = part.title

                Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isPart")
        }
}
