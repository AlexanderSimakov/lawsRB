package com.team.lawsrb.basic.codeObjects

/**
 * A *data class* that represents single codex Part.
 *
 * @constructor Creates single [Part] object.
 *
 * @property title Name of an [Part].
 * @property id Unique id.
 * @property isLiked Equals *true* if [Part] marked as favorite.
 */
data class Part(val title: String,
                val id: Int = 0,
                var isLiked: Boolean = false)