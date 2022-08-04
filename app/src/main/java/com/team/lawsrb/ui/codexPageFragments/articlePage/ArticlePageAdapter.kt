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
import com.team.lawsrb.basic.codexObjects.Article
import com.team.lawsrb.basic.codexObjects.Chapter

class ArticlePageAdapter (private val items: List<Any>, private val rvView: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val isArticle = 1
    private val isChapter = 2

    inner class ArticleViewHolder(articleCardView: View) : RecyclerView.ViewHolder(articleCardView) {
        val card: MaterialCardView = articleCardView.findViewById(R.id.dark_card_with_favorites)
        val title: TextView = articleCardView.findViewById(R.id.dark_card_with_favorites_title)
        val content: TextView = articleCardView.findViewById(R.id.dark_card_with_favorites_content)
        val checkBox: CheckBox = articleCardView.findViewById(R.id.dark_card_with_favorites_checkbox)
        val expandable: LinearLayout = articleCardView.findViewById(R.id.dark_card_with_favorites_expandable)
        val expandableText: TextView = articleCardView.findViewById(R.id.dark_card_with_favorites_expandable_text)
    }

    inner class ChapterViewHolder(chapterCardView: View) : RecyclerView.ViewHolder(chapterCardView) {
        val title: TextView = chapterCardView.findViewById(R.id.light_card_title)
        val content: TextView = chapterCardView.findViewById(R.id.light_card_content)
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
                val lightCardView = inflater.inflate(R.layout.dark_card_with_favorites, parent, false)
                ArticleViewHolder(lightCardView)
            }
            isChapter -> {
                val lightCardView = inflater.inflate(R.layout.light_card, parent, false)
                ChapterViewHolder(lightCardView)
            }
            else -> throw IllegalArgumentException("viewType was $viewType, expected $isArticle or $isChapter")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        when (viewHolder.itemViewType){
            isArticle -> {
                val article: Article = items[position] as Article
                (viewHolder as ArticleViewHolder).title.text = article.title
                viewHolder.content.text = "Section content"
                viewHolder.checkBox.isChecked = article.isLiked
                viewHolder.expandableText.text = article.content
                viewHolder.card.setOnClickListener {
                    TransitionManager.beginDelayedTransition(rvView as ViewGroup?, AutoTransition())
                    if (viewHolder.expandable.visibility == View.VISIBLE){
                        viewHolder.expandable.visibility = View.GONE
                    }else{
                        viewHolder.expandable.visibility = View.VISIBLE
                    }
                }
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                (viewHolder as ChapterViewHolder).title.text = chapter.title
                viewHolder.content.text = "Part content"
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isArticle or $isChapter")
        }
}
