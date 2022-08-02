package com.team.lawsrb.basic.codexObjects

/**
 * A *data class* that represents single codex article with items.
 *
 * @constructor Creates single [Article] object.
 *
 * @property title Name of an [Article].
 * @property id Unique id.
 * @property parentId Id of an [Article] parent.
 * @property isLiked Equals *true* if [Article] marked as favorite.
 * @property items Content items.
 * @property content Represent [items] as [String] with \n separator.
 */
data class Article(val title: String,
                   val id: Int = 0,
                   val parentId: Int = 0,
                   var isLiked: Boolean = false){

    val items = mutableListOf<String>()

    val content: String
        get() {
            var text = ""
            for (item in items) text += item + "\n"
            return text
        }
}
