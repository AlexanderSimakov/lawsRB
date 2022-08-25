package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Parser
{
    private var codexLists = CodexLists()
    private var contentList = mutableListOf<CodexContent>()
    private var articlesList = mutableListOf<CodexContent>()
    private var document: Document? = null

    fun get(codex: Codex): CodexLists {
        try {
            document = Jsoup.connect(codex.URL).maxBodySize(4_194_304).get()
        } catch (e: Exception) {
            Log.d("Error", "${e.message}")
        }

        codexLists.clear()
        contentList.clear()
        articlesList.clear()

        parsePartsTitles(codex)
        parseSectionsTitles(codex)
        parseChaptersTitles(codex)
        parseArticlesTitles(codex)
        parseContent(codex)
        articlesWithContent()

        return codexLists
    }

    private fun parsePartsTitles(codex: Codex)
    {
        try
        {
            val table = document!!.select("main")
            val elements = table.select("p")

            var partId = 1
            for (element in elements) {
                if (element.attr("class").equals("contenttext"))
                {
                    if (element.text().contains("ЧАСТЬ"))
                    {
                        val part = Part(element.text(), partId, false)
                        codexLists.parts.add(part)
                        partId++
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.d("Log", "Error : ${e.message}");
        }
    }

    private fun parseSectionsTitles(codex: Codex)
    {
        try
        {
            val table = document!!.select("main")
            val elements = table.select("p")

            var (parentId, sectionId) = List(2) { 0 }
            for (element in elements)
            {
                if (element.attr("class").equals("contenttext"))
                {
                    if (element.text().contains("РАЗДЕЛ"))
                    {
                        sectionId++
                        val section = Section(element.text(), sectionId, parentId, false)
                        codexLists.sections.add(section)
                    }
                    else if (element.text().contains("ЧАСТЬ")) {
                        parentId++
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.d("Log", "Error : ${e.message}");
        }
    }

    private fun parseChaptersTitles(codex: Codex)
    {
        try
        {
            val table = document!!.select("main")
            val elements = table.select("p")

            var (parentId, chapterId) = List(2) { 0 }
            for (element in elements)
            {
                if (element.attr("class").equals("contenttext"))
                {
                    if (element.text().contains("ГЛАВА"))
                    {
                        chapterId++
                        val chapter = Chapter(element.text(), chapterId, parentId, false)
                        codexLists.chapters.add(chapter)
                    }
                    else if (element.text().contains("ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ"))
                    {
                        if(!element.nextElementSibling().text().contains("ГЛАВА")){
                            parentId++
                            chapterId++
                            val chapter = Chapter("ГЛАВА. Заключительные положения", chapterId, parentId, false)
                            codexLists.chapters.add(chapter)
                        }
                    }
                    else if (element.text().contains("РАЗДЕЛ")) {
                        parentId++
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.d("Log", "Error : ${e.message}");
        }
    }

    private fun parseArticlesTitles(codex: Codex)
    {
        try
        {
            val table = document!!.select("main")
            val elements = table.select("p")

            var parentId = 0
            for (element in elements)
            {
                if (element.attr("class").equals("contenttext"))
                {
                    if (element.text().contains("Статья"))
                    {
                        val article = CodexContent(parentId, element.text())
                        articlesList.add(article)
                    }
                    else if (element.text().contains("ГЛАВА")){
                        parentId++
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.d("Log", "Error : ${e.message}");
        }
    }

    private fun parseContent(codex: Codex)
    {
        try
        {
            val table = document!!.select("main")
            val elements = table.select("p")

            var index: MutableList<Int> = mutableListOf()
            var currentId = 0

            for (element in elements)
            {
                if (element.attr("class").equals("article"))
                {
                    index.add(elements.indexOf(element))
                    break
                }
            }

            var toIndex: Int
            for (element in elements)
            {
                if (element.text().contains("А.Лукашенко"))
                {
                    toIndex = (elements.indexOf(element))
                    toIndex -= 2
                    index.add(toIndex)
                    break
                }
            }

            var parentId = articlesList[articlesList.size - 1].parentId
            val mainContent = elements.subList(index[0], index[1])
            for (element in mainContent)
            {
                if (element.attr("class").equals("article"))
                {
                    currentId++
                }

                if (element.text().contains("Настоящий Кодекс вводится в действие специальным законом."))
                {
                    if (!element.previousElementSibling().attr("class").equals("article"))
                    {
                        parentId++
                        currentId++
                        val articlesTitle = CodexContent(parentId, "Статья. Заключительные положения")
                        articlesList.add(articlesTitle)
                        val codexContent = CodexContent(currentId, element.text())
                        contentList.add(codexContent)
                        break
                    }
                }

                if (!element.attr("class").equals("article")
                    && !element.attr("class").equals("nonumheader")
                    && !element.attr("class").equals("zagrazdel")
                    && !element.attr("class").equals("chapter")
                    && !element.attr("class").equals("part")
                    && element.text() != "")
                {
                    val codexContent = CodexContent(currentId, element.text())
                    contentList.add(codexContent)
                }
            }
        }
        catch (e: Exception)
        {
            Log.d("Log", "Error : ${e.message}");
        }
    }

    private fun articlesWithContent()
    {
        var contentText = ""

        for ((id, articles) in articlesList.withIndex())
        {
            for (content in contentList)
            {
                if (content.parentId == id + 1) {
                    contentText += content.contentText + "\n"
                }
            }
            val article = Article(articles.contentText, id + 1, articles.parentId, false, contentText)
            codexLists.articles.add(article)
            contentText = ""
        }
    }
}