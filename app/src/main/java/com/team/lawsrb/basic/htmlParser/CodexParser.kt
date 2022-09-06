package com.team.lawsrb.basic.htmlParser

import android.util.Log
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object CodexParser {
    private var codexLists = CodexLists()
    private var contentList = mutableListOf<CodexContent>()
    private var articlesList = mutableListOf<CodexContent>()

    private var document: Document? = null

    private const val TAG = "CodexParser"

    fun get(codex: Codex): CodexLists? {
        try {
            document = Jsoup.connect(codex.URL).maxBodySize(4_194_304).get()
        }
        catch (e: Exception) {
            Log.e(TAG, "${e.message}: Getting document's error")
        }

        parsePartsTitles()
        parseSectionsTitles()
        parseChaptersTitles()
        parseArticlesTitles()
        parseContent()
        articlesWithContent()

        return codexLists
    }

    private fun parsePartsTitles() {
        try {
            val table = document!!.select("main")
            val elements = table.select("p")

            var partId = 1
            for (element in elements) {
                if (element.attr("class").equals("contenttext")) {
                    if (element.text().contains("ЧАСТЬ")) {
                        val part = Part(element.text(), partId, false)
                        codexLists.parts.add(part)
                        partId++
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "Error : ${e.message}: Error parsing parts titles");
        }
    }

    private fun parseSectionsTitles() {
        try {
            val table = document!!.select("main")
            val elements = table.select("p")

            var (parentId, sectionId) = List(2) { 0 }
            for (element in elements) {
                if (element.attr("class").equals("contenttext")) {
                    if (element.text().contains("РАЗДЕЛ")) {
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
        catch (e: Exception) {
            Log.e(TAG, "Error : ${e.message}: Error parsing sections titles");
        }
    }

    private fun parseChaptersTitles() {
        try {
            val table = document!!.select("main")
            val elements = table.select("p")

            var (parentId, chapterId) = List(2) { 0 }
            for (element in elements) {
                if (element.attr("class").equals("contenttext")) {
                    if (element.text().contains("ГЛАВА")) {
                        chapterId++
                        val chapter = Chapter(element.text(), chapterId, parentId, false)
                        codexLists.chapters.add(chapter)
                    }
                    else if (element.text().contains("ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ")) {
                        if(!element.nextElementSibling().text().contains("ГЛАВА")) {
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
        catch (e: Exception) {
            Log.e(TAG, "Error : ${e.message}: Error parsing chapters titles");
        }
    }

    private fun parseArticlesTitles() {
        try {
            val table = document!!.select("main")
            val elements = table.select("p")

            var parentId = 0
            for (element in elements) {
                if (element.attr("class").equals("contenttext")) {
                    if (element.text().contains("Статья")) {
                        if (element.attr("id").contains("/")) {
                            var title = element.toString()
                            title = formatText(title)
                            val article = CodexContent(parentId, title)
                            articlesList.add(article)
                        }
                        if (element.text().contains("в действие настоящего Кодекса")
                            && !element.previousElementSibling().text().contains("ГЛАВА")) {
                            parentId++
                            val article = CodexContent(parentId, element.text())
                            articlesList.add(article)
                        }
                        else if (!element.attr("id").contains("/")) {
                            val article = CodexContent(parentId, element.text())
                            articlesList.add(article)
                        }
                    }
                    else if (element.text().contains("ГЛАВА")) {
                        parentId++
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "Error : ${e.message}: Error parsing articles titles");
        }
    }

    private fun parseContent() {
        try {
            val table = document!!.select("main")
            val elements = table.select("p")

            var indices: MutableList<Int> = mutableListOf()
            var currentId = 0

            for (element in elements) {
                if (element.attr("class").equals("article")) {
                    indices.add(elements.indexOf(element))
                    break
                }
            }

            var toIndicesList: Int
            for (element in elements) {
                if (element.text().contains("А.Лукашенко")) {
                    toIndicesList = (elements.indexOf(element))
                    toIndicesList -= 2
                    indices.add(toIndicesList)
                    break
                }
            }

            var parentId = articlesList[articlesList.size - 1].parentId
            val mainContent = elements.subList(indices[0], indices[1])
            for (element in mainContent) {
                if (element.attr("class").equals("article")) {
                    currentId++
                }

                if (element.text().contains("Настоящий Кодекс вводится в действие специальным законом.")) {
                    if (!element.previousElementSibling().attr("class").equals("article")) {
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
                    && element.text() != "") {
                    var content = element.toString()
                    if (content.contains("<sup>")) {
                        content = formatText(content)
                        val codexContent = CodexContent(currentId, content)
                        Log.d(TAG, content)
                        contentList.add(codexContent)
                    }
                    else {
                        val codexContent = CodexContent(currentId, element.text())
                        Log.d(TAG, element.text())
                        contentList.add(codexContent)
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e(TAG, "Error : ${e.message}: Error parsing article's contents");
        }
    }

    private fun articlesWithContent() {
        var contentText = ""
        for ((id, articles) in articlesList.withIndex()) {
            for (content in contentList) {
                if (content.parentId == id + 1) {
                    contentText += content.contentText + "\n"
                }
            }
            val article = Article(articles.contentText, id + 1, articles.parentId, false, contentText)
            codexLists.articles.add(article)
            contentText = ""
        }
    }

    private fun formatText(content: String): String {
        var text = content

        text = text.replace("<sup>", "/")
        text = text.replace("</sup>", "")
        text = text.replace("(\\<[^<]+\\>\\s*)".toRegex(), " ")
        text = text.replace("&nbsp;", " ")
        text = text.replace("  ", " ")
        text = text.replace(" ,", ",")
        text = text.replace(" )", ")")
        text = text.replace("( ", "(")
        text = text.replace(" . ", ". ")
        text = text.replace(" /", "/")

        return text
    }
}