package com.team.lawsrb.basic.roomDatabase.codexObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase

/**
 * A *data class* that represents single codex Part.
 *
 * @constructor Creates single [Part] object.
 *
 * @property title Name of an [Part].
 * @property id Unique id.
 * @property isLiked Equals *true* if [Part] marked as favorite.
 */
@Entity(tableName = CriminalCodexDatabase.PARTS_NAME)
data class Part(
    val title: String,
    @PrimaryKey val id: Int,
    var isLiked: Boolean = false
)