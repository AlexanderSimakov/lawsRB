package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.lawsrb.basic.roomDatabase.*
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

/**
 * [BaseCodexProvider] is a *object* class which initialize
 * UK, UPK, KoAP and PIKoAP [CodexProvider]s,
 * provide single access point to them, and contain some manage methods.
 *
 * @see [CodexProvider]
 */
object BaseCodexProvider {
    /** This is a [CodexDatabase] which provide access to the UK codex tables. */
    val UK: CodexProvider
        get() = _UK

    /** This is a [CodexDatabase] which provide access to the UPK codex tables. */
    val UPK: CodexProvider
        get() = _UPK

    /** This is a [CodexDatabase] which provide access to the KoAP codex tables. */
    val KoAP: CodexProvider
        get() = _KoAP

    /** This is a [CodexDatabase] which provide access to the PIKoAP codex tables. */
    val PIKoAP: CodexProvider
        get() = _PIKoAP

    private val _UK = getCodex(BaseCodexDatabase.UK)
    private val _UPK = getCodex(BaseCodexDatabase.UPK)
    private val _KoAP = getCodex(BaseCodexDatabase.KoAP)
    private val _PIKoAP = getCodex(BaseCodexDatabase.PIKoAP)

    /** This method initialize and return [CodexProvider] for given [CodexDatabase]. */
    private fun getCodex(_database: CodexDatabase) = object: CodexProvider  {
        override val database = _database

        private var parts = emptyList<Part>()
        private var sections = emptyList<Section>()
        private var chapters = emptyList<Chapter>()
        private var articles = emptyList<Article>()

        /**
         * [sectionPageItems] are items *(Parts and Sections)*
         * which will shown on the **Section Page**.
         *
         * @see Part
         * @see Section
         */
        private val sectionPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

        /**
         * [chapterPageItems] are items *(Sections and Chapters)*
         * which will shown on the **Chapter Page**.
         *
         * @see Section
         * @see Chapter
         */
        private val chapterPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

        /**
         * [articlePageItems] are items *(Chapters and Articles)*
         * which will shown on the **Article Page**.
         *
         * @see Chapter
         * @see Article
         */
        private val articlePageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }


        override val isSectionPageItemsEmpty: Boolean
            get() = getSectionPageItems().value?.isEmpty() == true

        override val isChapterPageItemsEmpty: Boolean
            get() = getChapterPageItems().value?.isEmpty() == true

        override val isArticlePageItemsEmpty: Boolean
            get() = getArticlePageItems().value?.isEmpty() == true


        override val isSectionPageItemsNotEmpty: Boolean
            get() = getSectionPageItems().value?.isNotEmpty() == true

        override val isChapterPageItemsNotEmpty: Boolean
            get() = getChapterPageItems().value?.isNotEmpty() == true

        override val isArticlePageItemsNotEmpty: Boolean
            get() = getArticlePageItems().value?.isNotEmpty() == true


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

        fun updateAll(){
            loadAll()
            updatePageItemsAsync()
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
            val articlesByTitle = database.articlesDao().findByTitle("%${searchQuery}%")
            val articlesByContent = database.articlesDao().findByContent("%${searchQuery}%")
            val articles = articlesByTitle.toMutableList()
            articles.addAll(articlesByContent.filter {
                it.id !in articlesByTitle.map { article -> article.id }
            })

            articlePageItems.value = articles
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

        private fun updatePageItemsAsync(){
            // TODO: refactor same code
            val sectionItems = mutableListOf<Any>()
            for (part in parts){
                sectionItems.add(part)
                sectionItems.addAll(sections.filter { it.parentId == part.id })
            }
            sectionPageItems.postValue(sectionItems)

            val chapterItems = mutableListOf<Any>()
            for (section in sections){
                chapterItems.add(section)
                chapterItems.addAll(chapters.filter { it.parentId == section.id })
            }
            chapterPageItems.postValue(chapterItems)

            val articleItems = mutableListOf<Any>()
            for (chapter in chapters){
                articleItems.add(chapter)
                articleItems.addAll(articles.filter { it.parentId == chapter.id })
            }
            articlePageItems.postValue(articleItems)
        }
    }

    fun update(){
        _UK.updateAll()
        _UPK.updateAll()
        _KoAP.updateAll()
        _PIKoAP.updateAll()
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
