package com.team.lawsrb.basic.dataProviders

import com.team.lawsrb.basic.codeObjects.Article
import com.team.lawsrb.basic.codeObjects.Chapter
import com.team.lawsrb.basic.codeObjects.Part
import com.team.lawsrb.basic.codeObjects.Section

interface CodeProvider{
    fun getParts(): MutableList<Part>
    fun getSections(): MutableList<Section>
    fun getSections(part: Part): MutableList<Section>
    fun getChapters(): MutableList<Chapter>
    fun getChapters(section: Section): MutableList<Chapter>
    fun getArticles(): MutableList<Article>
    fun getArticles(chapter: Chapter): MutableList<Article>
}