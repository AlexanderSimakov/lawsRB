package com.requestfordinner.lawsrb.ui.codexPageFragments.items

import android.view.View
import androidx.core.view.isVisible
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.databinding.ArticleCardBinding
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.xwray.groupie.viewbinding.BindableItem

/** A [BindableItem] of Groupie representing article card. */
class ArticleItem(
    private val article: Article,
    private var isOpened: Boolean,
    private val onCardClick: (article: Article, item: ArticleCardBinding) -> Unit,
    private val onCheckBoxClick: (article: Article, isLiked: Boolean) -> Unit,
) : BindableItem<ArticleCardBinding>() {

    override fun bind(viewBinding: ArticleCardBinding, position: Int) {
        viewBinding.title.text = article.title
        viewBinding.content.text = article.content
        viewBinding.checkbox.isChecked = article.isLiked
        viewBinding.expandableLayout.isVisible = isOpened

        Highlighter.applyTo(viewBinding.title, BaseCodexProvider.search)
        Highlighter.applyTo(viewBinding.content, BaseCodexProvider.search)

        viewBinding.card.setOnClickListener {
            onCardClick(article, viewBinding)
        }

        viewBinding.checkbox.setOnClickListener {
            article.isLiked = viewBinding.checkbox.isChecked
            onCheckBoxClick(article, viewBinding.checkbox.isChecked)
        }
    }

    override fun getLayout() = R.layout.article_card

    override fun initializeViewBinding(view: View) = ArticleCardBinding.bind(view)

}