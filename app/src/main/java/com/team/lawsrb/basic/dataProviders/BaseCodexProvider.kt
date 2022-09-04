package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.lawsrb.basic.roomDatabase.*
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

object BaseCodexProvider {
    val UK: CodexProvider
        get() = _UK

    val UPK: CodexProvider
        get() = _UPK

    val KoAP: CodexProvider
        get() = _KoAP

    val PIKoAP: CodexProvider
        get() = _PIKoAP

    private val _UK = getCodex(BaseCodexDatabase.UK)
    private val _UPK = getCodex(BaseCodexDatabase.UPK)
    private val _KoAP = getCodex(BaseCodexDatabase.KoAP)
    private val _PIKoAP = getCodex(BaseCodexDatabase.PIKoAP)

    private fun getCodex(_database: CodexDatabase) = object: CodexProvider  {
        override val database = _database

        private var parts = emptyList<Part>()
        private var sections = emptyList<Section>()
        private var chapters = emptyList<Chapter>()
        private var articles = emptyList<Article>()

        private val sectionPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
        private val chapterPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
        private val articlePageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

        var searchQuery: String = ""
            set(value) {
                field = value
                update()
            }

        var isFavorites: Boolean = false
            set(value) {
                field = value
                update()
            }

        init {
            loadAll()
            updatePageItems()
        }

        override fun getSectionPageItems() = sectionPageItems as LiveData<List<Any>>
        override fun getChapterPageItems() = chapterPageItems as LiveData<List<Any>>
        override fun getArticlePageItems() = articlePageItems as LiveData<List<Any>>

        // call then change favorites or search
        private fun update(){
            if (searchQuery.isNotBlank() && !isFavorites){
                loadPageItemsBySearchQuery()
            }else{
                if (searchQuery.isNotBlank() && isFavorites){
                    loadFavorites()
                    adjustBySearchQuery()
                } else if (isFavorites){
                    loadFavorites()
                } else {
                    loadAll()
                }
                updatePageItems()
            }
        }

        private fun loadFavorites(){
            articles = database.articlesDao().getFavorites()
            chapters = database.chaptersDao().getByIds(articles.map { it.parentId })
            sections = database.sectionsDao().getByIds(chapters.map { it.parentId })
            parts = database.partsDao().getByIds(sections.map { it.parentId })
        }

        private fun loadPageItemsBySearchQuery(){
            articlePageItems.value = database.articlesDao().findAll("%${searchQuery}%")
            chapterPageItems.value = database.chaptersDao().findAll("%${searchQuery}%")
            sectionPageItems.value = database.sectionsDao().findAll("%${searchQuery}%")
        }

        private fun adjustBySearchQuery(){
            val pattern = searchQuery.toRegex(RegexOption.IGNORE_CASE)
            articles = articles.filter { pattern.containsMatchIn(it.title) || pattern.containsMatchIn(it.content) }
            chapters = chapters.filter { it.id in articles.map { article -> article.parentId } }
            sections = sections.filter { it.id in chapters.map { chapter -> chapter.parentId } }
            parts = parts.filter { it.id in sections.map { section -> section.parentId } }
        }

        private fun loadAll(){
            parts = database.partsDao().getAll()
            sections = database.sectionsDao().getAll()
            chapters = database.chaptersDao().getAll()
            articles = database.articlesDao().getAll()
        }

        private fun updatePageItems(){
            val sectionItems = mutableListOf<Any>()
            for (part in parts){
                sectionItems.add(part)
                sectionItems.addAll(sections.filter { it.parentId == part.id })
            }
            sectionPageItems.value = sectionItems

            val chapterItems = mutableListOf<Any>()
            for (section in sections){
                chapterItems.add(section)
                chapterItems.addAll(chapters.filter { it.parentId == section.id })
            }
            chapterPageItems.value = chapterItems

            val articleItems = mutableListOf<Any>()
            for (chapter in chapters){
                articleItems.add(chapter)
                articleItems.addAll(articles.filter { it.parentId == chapter.id })
            }
            articlePageItems.value = articleItems
        }
    }

    fun sentQuery(search: String){
        _UK.searchQuery = search
        _UPK.searchQuery = search
        _KoAP.searchQuery = search
        _PIKoAP.searchQuery = search
    }

    fun getQuery() = _UK.searchQuery

    fun setFavorite(isFavorite: Boolean){
        _UK.isFavorites = isFavorite
        _UPK.isFavorites = isFavorite
        _KoAP.isFavorites = isFavorite
        _PIKoAP.isFavorites = isFavorite
    }
}
