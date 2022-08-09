package com.team.lawsrb.basic.roomDatabase.codexObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase

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
@Entity(
    tableName = CriminalCodexDatabase.SECTIONS_NAME,
    foreignKeys = [
        ForeignKey (
            entity = Part::class,
            parentColumns = ["id"],
            childColumns = ["part_id"]
        )]
)
data class Section(
    val title: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "part_id") val parentId: Int,
    var isLiked: Boolean = false
)