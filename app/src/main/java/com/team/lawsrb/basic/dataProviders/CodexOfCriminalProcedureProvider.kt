package com.team.lawsrb.basic.dataProviders

import com.team.lawsrb.basic.codexObjects.Article
import com.team.lawsrb.basic.codexObjects.Chapter
import com.team.lawsrb.basic.codexObjects.Part
import com.team.lawsrb.basic.codexObjects.Section

object CodexOfCriminalProcedureProvider : CodexProvider {
    private val parts = mutableListOf<Part>()
    private val sections = mutableListOf<Section>()
    private val chapters = mutableListOf<Chapter>()
    private val articles = mutableListOf<Article>()

    init {
        for (id in 0..1) parts.add(Part("Part ${id+1}", id))
        for (id in 0..5) sections.add(Section("Section ${id+1}", id, if (id > 3) 1 else 0))
        for (id in 0..13) chapters.add(Chapter("Chapter ${id+1}", id, (id-1)/2))
        for (id in 0..27) articles.add(Article("Article ${id+1}", id, (id-1)/2))
    }

    override fun getParts() = parts
    override fun getSections() = sections
    override fun getChapters() = chapters
    override fun getArticles() = articles

    override fun getSections(part: Part): MutableList<Section>{
        val sections = mutableListOf<Section>()
        for (section in this.sections){
            if (section.parentId == part.id) sections.add(section)
        }
        return sections
    }

    override fun getChapters(section: Section): MutableList<Chapter>{
        val chapters = mutableListOf<Chapter>()
        for (chapter in this.chapters){
            if (chapter.parentId == section.id) chapters.add(chapter)
        }
        return chapters
    }

    override fun getArticles(chapter: Chapter): MutableList<Article>{
        val articles = mutableListOf<Article>()
        for (article in this.articles){
            if (article.parentId == chapter.id) articles.add(article)
        }
        return articles
    }
}