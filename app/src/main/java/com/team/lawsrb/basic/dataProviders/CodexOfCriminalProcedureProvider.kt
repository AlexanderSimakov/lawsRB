package com.team.lawsrb.basic.dataProviders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.lawsrb.basic.codexObjects.*

object CodexOfCriminalProcedureProvider : CodexProvider {
    private val parts = mutableListOf<Part>()
    private val sections = mutableListOf<Section>()
    private val chapters = mutableListOf<Chapter>()
    private val articles = mutableListOf<Article>()

    private val sectionPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val chapterPageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }
    private val articlePageItems: MutableLiveData<List<Any>> by lazy { MutableLiveData<List<Any>>() }

    var searchQuery: String = ""
        set(value: String) {
            field = value
            updateItems()
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
        for (id in 0..1) parts.add(Part("Part ${id+1}", id))
        for (id in 0..5) sections.add(Section("Section ${id+1}", id, if (id > 3) 1 else 0))
        for (id in 0..13) chapters.add(Chapter("Chapter ${id+1}", id, (id-1)/2))
        for (id in 0..27) articles.add(Article("Article ${id+1}", id, (id-1)/2))

        initLiveData()
    }

    private fun initLiveData(){
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

    override fun getSectionPageItems() = sectionPageItems as LiveData<List<Any>>

    override fun getChapterPageItems() = chapterPageItems as LiveData<List<Any>>

    override fun getArticlePageItems() = articlePageItems as LiveData<List<Any>>
}
