package com.requestfordinner.lawsrb.ui.codexPageFragments.articlePage

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.dataProviders.BaseCodexProvider
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.requestfordinner.lawsrb.ui.codexPageFragments.Highlighter
import com.requestfordinner.lawsrb.ui.codexPageFragments.PageNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ArticlePageAdapter] is a child of [RecyclerView.Adapter] which is used for creating
 * [Article] and [Chapter]'s views for **Article page**.
 *
 * @param items The list of all shown codex elements.
 * @param rvView [RecyclerView] which will show [items].
 * @param articlesDao Dao class for codex [Article]s.
 *
 * @see RecyclerView
 * @see RecyclerView.Adapter
 */
class ArticlePageAdapter(
    private val items: List<Any>,
    private val rvView: View,
    private val articlesDao: ArticlesDao,
    private val codeType: Codex
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** [CoroutineScope] for executing async operations */
    private val coroutine = CoroutineScope(Dispatchers.Main)

    private val TAG = "ArticlePageAdapter"

    private val isArticle = 1
    private val isChapter = 2

    /**
     * [ArticleViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Article card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
    class ArticleViewHolder(articleCardView: View) :
        RecyclerView.ViewHolder(articleCardView) {
        val card: MaterialCardView = articleCardView.findViewById(R.id.card)
        val title: TextView = articleCardView.findViewById(R.id.title)
        val checkBox: CheckBox = articleCardView.findViewById(R.id.checkbox)
        val expandable: LinearLayout = articleCardView.findViewById(R.id.expandable_layout)
        val expandableText: TextView = articleCardView.findViewById(R.id.content)
    }

    /**
     * [ChapterViewHolder] is a child of [RecyclerView.ViewHolder] which is
     * storing data that makes binding **Chapter card** view content easier.
     *
     * @see RecyclerView.ViewHolder
     */
    class ChapterViewHolder(chapterCardView: View) :
        RecyclerView.ViewHolder(chapterCardView) {
        val card: MaterialCardView = chapterCardView.findViewById(R.id.card)
        val title: TextView = chapterCardView.findViewById(R.id.title)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]::class) {
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
                setUpArticleView(article, viewHolder)
            }
            isChapter -> {
                val chapter: Chapter = items[position] as Chapter
                setUpChapterView(chapter, viewHolder)
            }
            else -> throw IllegalArgumentException("itemViewType was ${viewHolder.itemViewType}, expected $isArticle or $isChapter")
        }

    /** This method set up given Article [viewHolder] with given [article]. */
    private fun setUpArticleView(article: Article, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ArticleViewHolder).title.text = article.title
        viewHolder.checkBox.isChecked = article.isLiked
        viewHolder.expandableText.text = article.content

        viewHolder.card.setOnClickListener {
            TransitionManager.beginDelayedTransition(rvView as ViewGroup?, AutoTransition())
            if (viewHolder.expandable.visibility == View.VISIBLE) {
                viewHolder.expandable.visibility = View.GONE
                openedCodeArticles.getOrElse(codeType) { mutableSetOf() }.remove(article.id)
                Log.d(TAG, "$openedCodeArticles")
            } else {
                viewHolder.expandable.visibility = View.VISIBLE
                openedCodeArticles.getOrPut(codeType) { mutableSetOf() }.add(article.id)
                Log.d(TAG, "$openedCodeArticles")
            }
        }

        viewHolder.checkBox.setOnClickListener {
            article.isLiked = viewHolder.checkBox.isChecked
            coroutine.launch { articlesDao.update(article) }
        }

        if (article.id in openedCodeArticles.getOrElse(codeType) { mutableSetOf() }) {
            viewHolder.expandable.visibility = View.VISIBLE
        } else {
            viewHolder.expandable.visibility = View.GONE
        }

        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
        Highlighter.applyTo(viewHolder.expandableText, BaseCodexProvider.search)
    }

    /** This method set up given Chapter [viewHolder] with given [chapter]. */
    private fun setUpChapterView(chapter: Chapter, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as ChapterViewHolder).title.text = chapter.title
        viewHolder.card.setOnClickListener {
            PageNavigation.moveLeftTo(chapter)
        }

        Highlighter.applyTo(viewHolder.title, BaseCodexProvider.search)
    }

    companion object {
        private val openedCodeArticles = mutableMapOf(
            Codex.UK to mutableSetOf<Int>(),
            Codex.UPK to mutableSetOf<Int>(),
            Codex.KoAP to mutableSetOf<Int>(),
            Codex.PIKoAP to mutableSetOf<Int>()
        )
    }
}
