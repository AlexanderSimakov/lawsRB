package com.team.lawsrb.basic.codeObjects

/**
 * A *data class* that represents single codex chapter.
 *
 * @constructor Creates single [Chapter] object.
 *
 * @property title Name of an [Chapter].
 * @property id Unique id.
 * @property parentId Id of an [Chapter] parent.
 * @property isLiked Equals *true* if [Chapter] marked as favorite.
 */
data class Chapter(val title: String,
                   val id: Int = 0,
                   val parentId: Int = 0,
                   var isLiked: Boolean = false)