package com.requestfordinner.lawsrb.basic.htmlParser

import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article

/**
 * Class for storing and represent code data received from a web resource.
 * @constructor stores lists for data
 */
data class CodexLists(
    var parts: MutableList<Part> = mutableListOf(),
    var sections: MutableList<Section> = mutableListOf(),
    var chapters: MutableList<Chapter> = mutableListOf(),
    var articles: MutableList<Article> = mutableListOf()
)
