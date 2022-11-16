package com.requestfordinner.lawsrb.basic.htmlParser

import android.util.Log
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/** The class receives a code from the web resource for write to the database */

class CodexParser {
    /** Log tag field. */
    private val TAG = "CodexParser"

    /**
     * Data class, passed as a data type to the collection.
     *
     * Used in the collection that storing the titles of the articles
     * and in the collection that string the content of the articles.
     */
    private data class CodexContent(val parentId: Int, val contentText: String)

    private var codexLists = CodexLists()

    /**
     * List for storing articles content.
     * @see CodexContent
     */
    private var contentList = mutableListOf<CodexContent>()

    /**
     * List for storing articles titles.
     * @see CodexContent
     */
    private var articlesList = mutableListOf<CodexContent>()

    private lateinit var document: Document

    /** Array of elements retrieved from a web page using [Jsoup] */
    private lateinit var documentElements: Elements

    /**
     * [Jsoup] instance initialization function to parse html from web resource.
     * @param codex certain code from enum [Codex]
     * @return the code obtained from the web resource in [CodexLists] instance
     * @exception CodexParser can throw an exception?, NetworkException
     */
    fun get(codex: Codex): CodexLists {
        Log.i(TAG, "Start parse ${codex.name}")
        try {
            document = Jsoup.connect(codex.URL).maxBodySize(4_194_304).get()
            documentElements = document.select("main").select("p")
        } catch (e: Exception) {
            Log.e(TAG, "Getting document's error(${codex.name}): ${e.message}")
        }

        parse()
        Log.i(TAG, "End parse ${codex.name}")
        return codexLists
    }

    private fun parse() {
        parsePartsTitles()
        parseSectionsTitles()
        parseChaptersTitles()
        parseArticlesTitles()
        parseArticlesContent()
        articlesWithContent()
    }

    /**
     * The function parses html into parts titles.
     *
     * Html code is taken from [documentElements] array.
     */
    private fun parsePartsTitles() {
        var partId = 1
        for (element in documentElements) {
            if (element.attr("class").equals("contenttext")
                && element.text().contains("ЧАСТЬ")
            ) {
                codexLists.parts.add(
                    Part(element.text(), partId, false)
                )
                partId++
            }
        }
    }

    /**
     * The function parses html into sections titles.
     *
     * Html code is taken from [documentElements] array.
     */
    private fun parseSectionsTitles() {
        var (parentId, sectionId) = List(2) { 0 }

        for (element in documentElements) {
            if (element.attr("class").equals("contenttext")) {
                if (element.text().contains("РАЗДЕЛ")) {
                    sectionId++
                    codexLists.sections.add(
                        Section(element.text(), sectionId, parentId, false)
                    )
                } else if (element.text().contains("ЧАСТЬ")) {
                    parentId++
                }
            }
        }
    }

    /**
     * The function parses html into chapters titles.
     *
     * Html code is taken from [documentElements] array.
     */
    private fun parseChaptersTitles() {
        var (parentId, chapterId) = List(2) { 0 }

        for (element in documentElements) {
            if (element.attr("class").equals("contenttext")) {
                if (element.text().contains("ГЛАВА")) {
                    chapterId++
                    codexLists.chapters.add(
                        Chapter(formatText(element.toString()), chapterId, parentId, false)
                    )
                } else if (element.text().contains("ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ")
                    && !element.nextElementSibling()!!.text().contains("ГЛАВА")
                ) {
                    codexLists.chapters.add(
                        Chapter(
                            "ГЛАВА. Заключительные положения",
                            chapterId + 1,
                            parentId + 1,
                            false
                        )
                    )
                } else if (element.text().contains("РАЗДЕЛ")) {
                    parentId++
                }
            }
        }
    }

    /**
     * The function parses html into article titles.
     *
     * Html code is taken from [documentElements] array.
     */
    private fun parseArticlesTitles() {
        var parentId = 0

        for (element in documentElements) {
            if (element.attr("class").equals("contenttext")) {
                if (element.text().contains("Статья")) {
                    if (element.attr("id").contains("/")) {
                        articlesList.add(
                            CodexContent(parentId, formatText(element.toString()))
                        )
                    }

                    if (element.text().contains("в действие настоящего Кодекса")
                        && !element.previousElementSibling()!!.text().contains("ГЛАВА")
                    ) {
                        parentId++
                        articlesList.add(
                            CodexContent(parentId, element.text())
                        )
                    } else if (!element.attr("id").contains("/")) {
                        articlesList.add(
                            CodexContent(parentId, element.text())
                        )
                    }
                } else if (element.text().contains("ГЛАВА")) {
                    parentId++
                }
            }
        }
    }

    /**
     * The function parses html into article content.
     *
     * Html code is taken from [documentElements] array.
     */
    private fun parseArticlesContent() {
        val indices = mutableListOf<Int>()

        for (element in documentElements) {
            if (element.attr("class").equals("article")) {
                indices.add(documentElements.indexOf(element))
                break
            }
        }

        for (element in documentElements) {
            if (element.text().contains("А.Лукашенко")) {
                indices.add(documentElements.indexOf(element) - 2)
                break
            }
        }

        var currentId = 0
        val mainContent = documentElements.subList(indices[0], indices[1])
        for (element in mainContent) {
            if (element.attr("class").equals("article")) {
                currentId++
            }

            if (element.text().contains("Настоящий Кодекс вводится в действие специальным законом.")
                && !element.previousElementSibling()!!.attr("class").equals("article")
            ) {

                val parentId = articlesList[articlesList.size - 1].parentId
                articlesList.add(
                    CodexContent(parentId + 1, "Статья. Заключительные положения")
                )
                contentList.add(CodexContent(currentId + 1, element.text()))
                break
            }

            val elementClass = element.attr("class")
            if (!elementClass.equals("article") && !elementClass.equals("nonumheader") &&
                !elementClass.equals("zagrazdel") && !elementClass.equals("chapter") &&
                !elementClass.equals("part") && element.text() != ""
            ) {
                val content = element.toString()
                if (content.contains("<sup>")) {
                    //Log.d(TAG, formatText(content))
                    contentList.add(CodexContent(currentId, formatText(content)))
                } else {
                    //Log.d(TAG, element.text())
                    contentList.add(CodexContent(currentId, element.text()))
                }
            }
        }
    }

    /** Function connects articles titles and articles content. */
    private fun articlesWithContent() {
        var contentText = ""
        var countIterations = 0

        for ((id, article) in articlesList.withIndex()) {

            for (content in contentList) {
                if (content.parentId == id + 1) {
                    countIterations++
                    contentText += when (countIterations) {
                        1 -> content.contentText
                        else -> "\n" + content.contentText
                    }
                }
            }

            codexLists.articles.add(
                Article(article.contentText, id + 1, article.parentId, false, contentText)
            )

            contentText = ""
            countIterations = 0
        }
    }

    /**
     * The function converts the passed html into the finished text.
     * @param htmlCode received from a web resource
     */
    private fun formatText(htmlCode: String): String {
        var formattedText = htmlCode

        if (formattedText.contains("<sup></sup>"))
            formattedText = formattedText.replace("<sup></sup>", "")

        while (formattedText.contains("<sup>")) {
            for ((key, value) in fractionalParts) if (formattedText.contains(key))
                formattedText = formattedText.replace(key, value)
        }

        formattedText = formattedText
            .replace("(<[^<]+>\\s*)".toRegex(), " ")
            .replace("&nbsp;", " ")
            .replace("  ", " ")
            .replace(" ,", ",")
            .replace(" )", ")")
            .replace("( ", "(")
            .replace(" . ", ". ")
            .replace(" /", "/")

        return formattedText
    }

    private val fractionalParts = mapOf(
        "<sup>1</sup>" to "¹", "<sup>2</sup>" to "²", "<sup>3</sup>" to "³", "<sup>4</sup>" to "⁴",
        "<sup>5</sup>" to "⁵", "<sup>6</sup>" to "⁶", "<sup>7</sup>" to "⁷", "<sup>8</sup>" to "⁸",
        "<sup>9</sup>" to "⁹", "<sup>10</sup>" to "¹⁰", "<sup>11</sup>" to "¹¹",
        "<sup>12</sup>" to "¹²", "<sup>13</sup>" to "¹³", "<sup>14</sup>" to "¹⁴",
        "<sup>15</sup>" to "¹⁵", "<sup>16</sup>" to "¹⁶", "<sup>17</sup>" to "¹⁷",
        "<sup>18</sup>" to "¹⁸", "<sup>19</sup>" to "¹⁹", "<sup>20</sup>" to "²⁰",
        "<sup>21</sup>" to "²¹", "<sup>22</sup>" to "²²", "<sup>23</sup>" to "²³",
        "<sup>24</sup>" to "²⁴", "<sup>25</sup>" to "²⁵", "<sup>26</sup>" to "²⁶",
        "<sup>27</sup>" to "²⁷", "<sup>28</sup>" to "²⁸", "<sup>29</sup>" to "²⁹",
        "<sup>30</sup>" to "³⁰",
    )
}
