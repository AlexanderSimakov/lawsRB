package com.team.lawsrb.basic.htmlParser

import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article

data class CodexLists (
    var parts: MutableList<Part> = mutableListOf(),
    var sections: MutableList<Section> = mutableListOf(),
    var chapters: MutableList<Chapter> = mutableListOf(),
    var articles: MutableList<Article> = mutableListOf()
){
    fun clear(){
        parts.clear()
        sections.clear()
        chapters.clear()
        articles.clear()
    }
}



