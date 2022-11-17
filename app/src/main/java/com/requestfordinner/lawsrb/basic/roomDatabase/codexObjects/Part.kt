package com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.requestfordinner.lawsrb.basic.roomDatabase.CodexDatabase

/**
 * A *data class* that represents single codex Part.
 *
 * @property title Name of an [Part].
 * @property id Unique id.
 * @property isLiked Equals *true* if [Part] marked as favorite.
 *
 * @constructor Creates single [Part] object.
 */
@Entity(tableName = CodexDatabase.PARTS_NAME)
data class Part(
    override val title: String,
    @PrimaryKey val id: Int,
    var isLiked: Boolean = false
) : TitledItem