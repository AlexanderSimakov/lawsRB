package com.requestfordinner.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.htmlParser.CodexLists
import com.requestfordinner.lawsrb.basic.roomDatabase.*
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [BaseCodexProvider] is a *object* class which initialize
 * UK, UPK, KoAP and PIKoAP [CodexProvider]s,
 * provide single access point to them, and contain some manage methods.
 *
 * @see [CodexProvider]
 */
object BaseCodexProvider {

    /** This is a [CodexDatabase] which provide access to the UK codex tables. */
    val UK: CodexProvider get() = _UK

    /** This is a [CodexDatabase] which provide access to the UPK codex tables. */
    val UPK: CodexProvider get() = _UPK

    /** This is a [CodexDatabase] which provide access to the KoAP codex tables. */
    val KoAP: CodexProvider get() = _KoAP

    /** This is a [CodexDatabase] which provide access to the PIKoAP codex tables. */
    val PIKoAP: CodexProvider get() = _PIKoAP

    /**
     * Return [CodexProvider] by given [Codex].
     *
     * You can also use [BaseCodexProvider] property [UK], [UPK], [KoAP], [PIKoAP]
     * for this purpose.
     */
    fun get(codex: Codex): CodexProvider {
        return when (codex) {
            Codex.UK -> UK
            Codex.UPK -> UPK
            Codex.KoAP -> KoAP
            Codex.PIKoAP -> PIKoAP
        }
    }

    /**
     * This field updates page items for each codex to show matching [search] query
     * [Article]s, [Chapter]s and [Section]s.
     *
     * This method don't save codex hierarchy (**Article Page** will consist of [Article]s only).
     * But using [search] with [showFavorites] = `true` will save hierarchy.
     */
    var search: String = ""
        set(value) {
            field = value

            _UK.search = value
            _UPK.search = value
            _KoAP.search = value
            _PIKoAP.search = value
        }

    /**
     * This field updates page items for each codex to show favorite [Article]s if equals `true`.
     * If equals `false` then page items will contain a full codex.
     *
     * This method save codex hierarchy.
     */
    var showFavorites: Boolean = false
        set(value) {
            field = value

            _UK.showFavorites = value
            _UPK.showFavorites = value
            _KoAP.showFavorites = value
            _PIKoAP.showFavorites = value
        }

    /**
     * This method load and set page items from database for each codex
     * without influence of [search] and [showFavorites].
     */
    fun setDefaultPageItems() {
        _UK.setDefaultPageItems()
        _UPK.setDefaultPageItems()
        _KoAP.setDefaultPageItems()
        _PIKoAP.setDefaultPageItems()
    }

    private val _UK = getCodex(Codex.UK)
    private val _UPK = getCodex(Codex.UPK)
    private val _KoAP = getCodex(Codex.KoAP)
    private val _PIKoAP = getCodex(Codex.PIKoAP)

    /**
     * This method initialize and return [CodexProvider] for given [Codex].
     */
    private fun getCodex(_codeType: Codex) = object : CodexProvider {

        /** A [CoroutineScope] for executing async operations  */
        private val coroutine = CoroutineScope(Dispatchers.Main)

        /** Code type corresponding to code database */
        override val codeType: Codex = _codeType

        override val database = BaseCodexDatabase.get(_codeType)

        /**
         * [codex] contain all codex lists: parts, sections, chapters and articles.
         * This variable is used as a buffer between database and page items.
         */
        private val codex = CodexLists()

        /**
         * [sectionPageItems] are items *(Parts and Sections)*
         * which will shown on the **Section Page**.
         *
         * When update this field it also updates ui **Section Page**.
         *
         * @see Part
         * @see Section
         */
        private val sectionPageItems = MutableLiveData<List<Any>>(emptyList())

        /**
         * [chapterPageItems] are items *(Sections and Chapters)*
         * which will shown on the **Chapter Page**.
         *
         * When update this field it also updates ui **Chapter Page**.
         *
         * @see Section
         * @see Chapter
         */
        private val chapterPageItems = MutableLiveData<List<Any>>(emptyList())

        /**
         * [articlePageItems] are items *(Chapters and Articles)*
         * which will shown on the **Article Page**.
         *
         * When update this field it also updates ui **Article Page**.
         *
         * @see Chapter
         * @see Article
         */
        private val articlePageItems = MutableLiveData<List<Any>>(emptyList())

        override fun getSectionPageItems() = sectionPageItems as LiveData<List<Any>>
        override fun getChapterPageItems() = chapterPageItems as LiveData<List<Any>>
        override fun getArticlePageItems() = articlePageItems as LiveData<List<Any>>

        override val isSectionPageItemsEmpty: Boolean
            get() = sectionPageItems.value?.isEmpty() == true

        override val isChapterPageItemsEmpty: Boolean
            get() = chapterPageItems.value?.isEmpty() == true

        override val isArticlePageItemsEmpty: Boolean
            get() = articlePageItems.value?.isEmpty() == true


        override val isSectionPageItemsNotEmpty: Boolean
            get() = sectionPageItems.value?.isNotEmpty() == true

        override val isChapterPageItemsNotEmpty: Boolean
            get() = chapterPageItems.value?.isNotEmpty() == true

        override val isArticlePageItemsNotEmpty: Boolean
            get() = articlePageItems.value?.isNotEmpty() == true


        /**
         * This field updates page items to show matching [search] query
         * [Article]s, [Chapter]s and [Section]s.
         *
         * This method don't save codex hierarchy (**Article Page** will consist of [Article]s only).
         * But using [search] with [showFavorites] = `true` will save hierarchy.
         */
        var search: String = ""
            set(value) {
                field = value
                coroutine.launch { update() }
            }

        /**
         * This field updates page items to show favorite [Article]s if equals `true`.
         * If equals `false` then page items will contain a full codex.
         *
         * This method save codex hierarchy.
         */
        var showFavorites: Boolean = false
            set(value) {
                field = value
                coroutine.launch { update() }
            }

        init {
            coroutine.launch {
                loadCodexLists()
                formPageItemsAsync()
            }
        }

        /**
         * This method load and set page items from database
         * without influence of [search] and [showFavorites].
         */
        fun setDefaultPageItems() {
            coroutine.launch {
                loadCodexLists()
                formPageItemsAsync()
            }
        }

        /**
         * This method update page items using [search] and [showFavorites].
         */
        private suspend fun update() {
            if (search.isNotBlank() && !showFavorites) {
                loadAndSetPageItemsBySearchQuery()
            } else {
                if (search.isNotBlank() && showFavorites) {
                    loadFavoritesCodexLists()
                    adjustCodexListsBySearchQuery()
                } else if (showFavorites) {
                    loadFavoritesCodexLists()
                } else {
                    loadCodexLists()
                }
                formPageItemsAsync()
            }
        }

        /**
         * This method load only [Article]s, [Chapter]s and [Section]s
         * which matching [search] query. Write them to codex page items.
         */
        private suspend fun loadAndSetPageItemsBySearchQuery() {
            val articlesByTitle = database.articlesDao().findByTitle("%$search%")
            val articlesByContent = database.articlesDao().findByContent("%$search%")
            val articles = articlesByTitle.toMutableList()
            articles.addAll(articlesByContent.filter {
                it.id !in articlesByTitle.map { article -> article.id }
            })

            articlePageItems.value = articles
            chapterPageItems.value = database.chaptersDao().findAll("%$search%")
            sectionPageItems.value = database.sectionsDao().findAll("%$search%")
        }

        /**
         * This method load favorites [Article]s from [database]
         * and load this [Article]'s [Chapter]s, [Section]s and [Part]s. Save hierarchy.
         */
        private suspend fun loadFavoritesCodexLists() {
            codex.apply {
                articles = database.articlesDao().getFavorites() as MutableList
                chapters = database.chaptersDao()
                    .getByIds(articles.map { it.parentId }) as MutableList
                sections = database.sectionsDao()
                    .getByIds(chapters.map { it.parentId }) as MutableList
                parts = database.partsDao().getByIds(sections.map { it.parentId }) as MutableList
            }
        }

        /**
         * This method change [codex] lists to match [search] query.
         * Use it before [loadFavoritesCodexLists], because this method save
         * codex hierarchy.
         */
        private fun adjustCodexListsBySearchQuery() {
            val pattern = search.toRegex(RegexOption.IGNORE_CASE)

            codex.apply {
                articles = articles.filter {
                    pattern.containsMatchIn(it.title) || pattern.containsMatchIn(it.content)
                } as MutableList

                chapters = chapters.filter {
                    it.id in articles.map { article -> article.parentId }
                } as MutableList

                sections = sections.filter {
                    it.id in chapters.map { chapter -> chapter.parentId }
                } as MutableList

                parts = parts.filter {
                    it.id in sections.map { section -> section.parentId }
                } as MutableList
            }
        }

        /** This method load codex lists from [database] and update [codex]. */
        private suspend fun loadCodexLists() {
            codex.apply {
                parts = database.partsDao().getAll() as MutableList
                sections = database.sectionsDao().getAll() as MutableList
                chapters = database.chaptersDao().getAll() as MutableList
                articles = database.articlesDao().getAll() as MutableList
            }
        }

        /**
         * This method form page items from codex lists and
         * update them using `postValue()`.
         *
         * @see getFormedArticlePageItems
         * @see getFormedChapterPageItems
         * @see getFormedSectionPageItems
         */
        private fun formPageItemsAsync() {
            sectionPageItems.postValue(getFormedSectionPageItems())
            chapterPageItems.postValue(getFormedChapterPageItems())
            articlePageItems.postValue(getFormedArticlePageItems())
        }

        /**
         * It use [Section]s and [Part]s from [codex] to return formed
         * section page list for [sectionPageItems].
         *
         * @see sectionPageItems
         */
        private fun getFormedSectionPageItems(): MutableList<Any> {
            val sectionPage = mutableListOf<Any>()
            for (part in codex.parts) {
                sectionPage.add(part)
                sectionPage.addAll(codex.sections.filter { it.parentId == part.id })
            }
            return sectionPage
        }

        /**
         * It use [Chapter]s and [Section]s from [codex] to return formed
         * chapter page list for [chapterPageItems].
         *
         * @see chapterPageItems
         */
        private fun getFormedChapterPageItems(): MutableList<Any> {
            val chapterPage = mutableListOf<Any>()
            for (section in codex.sections) {
                chapterPage.add(section)
                chapterPage.addAll(codex.chapters.filter { it.parentId == section.id })
            }
            return chapterPage
        }

        /**
         * It use [Article]s and [Chapter]s from [codex] to return formed
         * page list for [articlePageItems].
         *
         * @see articlePageItems
         */
        private fun getFormedArticlePageItems(): MutableList<Any> {
            val articlePage = mutableListOf<Any>()
            for (chapter in codex.chapters) {
                articlePage.add(chapter)
                articlePage.addAll(codex.articles.filter { it.parentId == chapter.id })
            }
            return articlePage
        }
    }
}
