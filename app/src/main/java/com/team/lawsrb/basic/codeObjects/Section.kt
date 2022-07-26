package com.team.lawsrb.basic.codeObjects

/**
 * A *data class* that represents single codex section.
 *
 * @constructor Creates single [Section] object.
 *
 * @property title Name of an [Section].
 * @property id Unique id.
 * @property parentId Id of an [Section] parent.
 * @property isLiked Equals *true* if [Section] marked as favorite.
 */
data class Section(val title: String,
                   val id: Int = 0,
                   val parentId: Int = 0,
                   var isLiked: Boolean = false)