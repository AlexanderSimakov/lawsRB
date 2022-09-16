package com.team.lawsrb.ui.codexPageFragments.articlePage

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.team.lawsrb.R
import com.team.lawsrb.basic.dataProviders.BaseCodexProvider
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.team.lawsrb.ui.codexPageFragments.Highlighter
import com.team.lawsrb.ui.codexPageFragments.PageNavigation

class ArticlePageAdapter (private val items: List<Any>,
                          private val rvView: View,
                          private val articlesDao: ArticlesDao) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val isArticle = 1
    private val isChapter = 2

    inner class ArticleViewHolder(articleCardView: View) : RecyclerView.ViewHolder(articleCardView) {
        val card: MaterialCardView = articleCardView.findViewById(R.id.article_card)
        val title: TextView = articleCardView.findViewById(R.id.article_card_title)
        val checkBox: CheckBox = articleCardView.findViewById(R.id.article_card_checkbox)
        val expandable: LinearLayout = articleCardView.findViewById(R.id.article_card_expandable_layout)
        val expandableText: TextView = articleCardView.findViewById(R.id.article_card_content)
    }

    inner class ChapterViewHolder(chapterCardView: View) : RecyclerView.ViewHolder(chapterCardView) {
        val card: MaterialCardView = chapterCardView.findViewById(R.id.title_card)
        val title: TextView = chapterCardView.findViewById(R.id.title_card_title)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]::class){
            Article::class -> isArticle
            Chapter::class -> isChapter
            else -> throw IllegalArgumentException("items: List<Any> contain class ${items[position]::class.simpleName}, expected Article or Chapter")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            isArticle -> {
                val lightCardView = inflater.inflate(R.layout.article_card, parent, false)
                ArticleViewHolder(lightCardView)
            }
            isChapter -> {
                val lightCardView = inflater.inflate(R.layout.title_card, parent, false)
                ChapterViewHolder(lightCardView)
            }
            else -> throw IllegalArgumentException("viewType was $viewType, expected $isArticle or $isChapter")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        when (viewHolder.itemViewType) {
            isArticle -> {
                val article: Article = items[position] as Article
                (viewHolder as ArticleViewHolder).title.text = article.title
                viewHolder.checkBox.isChecked = article.isLiked
                viewHolder.expandableText.text = article.content

                if (openedArticleIds.isNotEmpty()){
                    viewHolder.expandable.visibility = View.VISIBLE
                }

                viewHolder.card.setOnClickListener {
                    TransitionManager.beginDelayedTransition(rvView as ViewGroup?, AutoTransition())
                    if (viewHolder.expandable.visibility == View.VISIBLE) {
                        viewHolder.expandable.visibility = View.GONE
                        openedArticleIds.remove(article.id)
                    } else {
                        viewHolder.expandable.visibility = View.VISIBLE
                        openedArticleIds.add(article.id)
                    }
                }

                viewHolder.checkBox.setOnClickListener {
                    article.isLiked = viewHolder.checkBox.isChecked
                    articlesDao.update(article)
                }

                if (article.id in openedArticleIds) {
                    viewHolder.expandable.visibility = View.VISIBLE
                } else {
                    viewHolder.expandable.visibility = View.GONE
                }

                Highlighter.applyTo(viewHolder.title, BaseCodexProvider.getQuery())
                Highlighter.applyTo(viewHolder.expandableText, BaseCodexProvider.getQuery())
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                (viewHolder as ChapterViewHolder).title.text = chapter.title
                viewHolder.card.setOnClickListener {
                    PageNavigation.moveLeftTo(chapter)
                }

                Highlighter.applyTo(viewHolder.title, BaseCodexProvider.getQuery())
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isArticle or $isChapter")
        }

    companion object {
        private val openedArticleIds = mutableSetOf<Int>()
    }
}
