package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.*

object CriminalCodexProvider: CodexProvider {
    private var parts = mutableListOf<Part>()
    private var sections = mutableListOf<Section>()
    private var chapters = mutableListOf<Chapter>()
    private var articles = mutableListOf<Article>()

    private val sectionPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val chapterPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val articlePageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

    override val database = CriminalCodexDatabase.getInstance()

    var searchQuery: String = ""
        set(value) {
            field = value
            if (field != "") updateItems()
            else initLiveData()
        }

    var isFavorites: Boolean = false
        set(value) {
            field = value
            if (field) showFavorites()
            else initLiveData()
        }

    private fun showFavorites(){
        articles = database.articlesDao().getFavorites() as MutableList<Article>
        chapters = database.chaptersDao().getByIds(articles.map { it.parentId }) as MutableList<Chapter>
        sections = database.sectionsDao().getByIds(chapters.map { it.parentId }) as MutableList<Section>
        parts = database.partsDao().getByIds(sections.map { it.parentId }) as MutableList<Part>
        initLiveData(false)
    }

    private fun updateItems(){
        // Example code
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
    }

    init {
        initLiveData()
    }

    private fun initLiveData(clearPrevious: Boolean = true){
        if (clearPrevious) initCodexLists()

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

    private fun initCodexLists(){
        parts = database.partsDao().getAll() as MutableList<Part>
        sections = database.sectionsDao().getAll() as MutableList<Section>
        chapters = database.chaptersDao().getAll() as MutableList<Chapter>
        articles = database.articlesDao().getAll() as MutableList<Article>
    }

    override fun getSectionPageItems() = sectionPageItems as LiveData<List<Any>>

    override fun getChapterPageItems() = chapterPageItems as LiveData<List<Any>>

    override fun getArticlePageItems() = articlePageItems as LiveData<List<Any>>
}
