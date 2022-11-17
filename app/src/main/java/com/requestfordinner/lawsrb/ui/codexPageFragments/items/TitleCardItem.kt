package com.requestfordinner.lawsrb.ui.codexPageFragments.items

import android.view.View
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.TitledItem
import com.requestfordinner.lawsrb.databinding.TitleCardBinding
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.xwray.groupie.viewbinding.BindableItem

/** A [BindableItem] of Groupie representing title card. */
class TitleCardItem(
    private val item: TitledItem,
    private val onClick: (item: TitledItem) -> Unit,
) : BindableItem<TitleCardBinding>() {

    override fun bind(viewBinding: TitleCardBinding, position: Int) {
        viewBinding.title.text = item.title
        viewBinding.card.setOnClickListener { onClick(item) }
        Highlighter.applyTo(viewBinding.title, BaseCodexProvider.search)
    }

    override fun getLayout(): Int = R.layout.title_card

    override fun initializeViewBinding(view: View) = TitleCardBinding.bind(view)
}
