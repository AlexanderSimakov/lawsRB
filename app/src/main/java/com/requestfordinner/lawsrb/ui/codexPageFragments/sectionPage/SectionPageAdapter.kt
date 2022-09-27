package com.requestfordinner.lawsrb.ui.codexPageFragments.sectionPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation

/**
 * [SectionPageAdapter] is a child of [RecyclerView.Adapter] which is used for creating
 * [Section] and [Part]'s views for **Section page**.
 *
 * @param items The list of all shown codex elements.
 *
 * @see RecyclerView
 * @see RecyclerView.Adapter
 */
class SectionPageAdapter (private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isSection = 1
    private val isPart = 2

    /**
     * [PartViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Part card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
    inner class PartViewHolder(partCardView: View) : RecyclerView.ViewHolder(partCardView) {
        val title: TextView = partCardView.findViewById(R.id.title_card_title)
    }

    /**
     * [SectionViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Section card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
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
                setUpSectionView(section, viewHolder)
            }
            isPart -> {
                val part: Part = items[position] as Part
                setUpPartView(part, viewHolder)
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isSection or $isPart")
        }

    /** This method set up given Section [viewHolder] with given [section]. */
    private fun setUpSectionView(section: Section, viewHolder: RecyclerView.ViewHolder){
        (viewHolder as SectionViewHolder).title.text = section.title
        viewHolder.card.setOnClickListener {
            PageNavigation.moveRightTo(section)
        }

        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
    }

    /** This method set up given Part [viewHolder] with given [part]. */
    private fun setUpPartView(part: Part, viewHolder: RecyclerView.ViewHolder){
        (viewHolder as PartViewHolder).title.text = part.title
        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
    }
}
