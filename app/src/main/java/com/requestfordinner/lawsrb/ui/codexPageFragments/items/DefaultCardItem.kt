package com.requestfordinner.lawsrb.ui.codexPageFragments.items

import android.view.View
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.TitledItem
import com.requestfordinner.lawsrb.databinding.ItemCardBinding
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.xwray.groupie.viewbinding.BindableItem

/** A [BindableItem] of Groupie representing default card. */
class DefaultCardItem(
    private val item: TitledItem,
    private val onClick: (item: TitledItem) -> Unit,
) : BindableItem<ItemCardBinding>() {

    override fun bind(viewBinding: ItemCardBinding, position: Int) {
        viewBinding.title.text = item.title
        viewBinding.card.setOnClickListener { onClick(item) }
        Highlighter.applyTo(viewBinding.title, BaseCodexProvider.search)
    }

    override fun getLayout() = R.layout.item_card

    override fun initializeViewBinding(view: View) = ItemCardBinding.bind(view)
}