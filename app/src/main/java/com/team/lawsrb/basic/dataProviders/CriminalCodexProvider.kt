package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.*

object CriminalCodexProvider: CodexProvider {
    private val parts = mutableListOf<Part>()
    private val sections = mutableListOf<Section>()
    private val chapters = mutableListOf<Chapter>()
    private val articles = mutableListOf<Article>()

    private val sectionPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val chapterPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val articlePageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

    override val database = CriminalCodexDatabase.getInstance()

    var searchQuery: String = ""
        set(value) {
            field = value
            updateItems()
        }

    var isFavorites: Boolean = false
        set(value) {
            field = value
            if (field) showFavorites()
            else initLiveData()
        }

    private fun showFavorites(){
        // TODO change algorithm to work with navigation
        val favorites = database.articlesDao().getAll()
        val favoritesArticles = mutableListOf<Any>()
        for (item: Any in favorites){
            if (item::class == Article::class && (item as Article).isLiked){
                favoritesArticles.add(item)
            }
        }
        articlePageItems.value = favoritesArticles
    }

    private fun updateItems(){
        // Example code
        if (searchQuery != ""){
            val pattern = searchQuery.toRegex(RegexOption.IGNORE_CASE)

            val articleItems = mutableListOf<Any>()
            for (article in articles){
                if (pattern.containsMatchIn(article.title) ||
                    pattern.containsMatchIn(article.content)){
                    articleItems.add(article)
                }
            }
            articlePageItems.value = articleItems

            val chapterItems = mutableListOf<Any>()
            for (chapter in chapters){
                if (pattern.containsMatchIn(chapter.title)){
                    chapterItems.add(chapter)
                }
            }
            chapterPageItems.value = chapterItems

            val sectionItems = mutableListOf<Any>()
            for (section in sections){
                if (pattern.containsMatchIn(section.title)){
                    sectionItems.add(section)
                }
            }
            sectionPageItems.value = sectionItems

        }else{
            initLiveData()
        }
    }

    init {
        initLiveData()
    }

    private fun initLiveData(){
        clearCodexLists()
        initCodexLists()

        val sectionItems = mutableListOf<Any>()
        for (part in parts){
            sectionItems.add(part)
            sections.filter { it.parentId == part.id }
                .forEach { sectionItems.add(it) }
        }
        sectionPageItems.value = sectionItems

        val chapterItems = mutableListOf<Any>()
        for (section in sections){
            chapterItems.add(section)
            chapters.filter { it.parentId == section.id }
                .forEach { chapterItems.add(it) }
        }
        chapterPageItems.value = chapterItems

        val articleItems = mutableListOf<Any>()
        for (chapter in chapters){
            articleItems.add(chapter)
            articles.filter { it.parentId == chapter.id }
                .forEach { articleItems.add(it) }
        }
        articlePageItems.value = articleItems
    }

    private fun clearCodexLists(){
        parts.clear()
        sections.clear()
        chapters.clear()
        articles.clear()
    }

    private fun initCodexLists(){
        parts.addAll(database.partsDao().getAll())
        sections.addAll(database.sectionsDao().getAll())
        chapters.addAll(database.chaptersDao().getAll())
        articles.addAll(database.articlesDao().getAll())
    }

    override fun getSectionPageItems() = sectionPageItems as LiveData<List<Any>>

    override fun getChapterPageItems() = chapterPageItems as LiveData<List<Any>>

    override fun getArticlePageItems() = articlePageItems as LiveData<List<Any>>
}
