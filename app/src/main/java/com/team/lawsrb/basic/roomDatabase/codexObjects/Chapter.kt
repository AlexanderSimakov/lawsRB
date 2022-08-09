package com.team.lawsrb.basic.roomDatabase.codexObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase

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
@Entity(
    tableName = CriminalCodexDatabase.CHAPTERS_NAME,
    foreignKeys = [
        ForeignKey (
            entity = Section::class,
            parentColumns = ["id"],
            childColumns = ["section_id"]
        )]
)
data class Chapter(
    val title: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "section_id") val parentId: Int,
    var isLiked: Boolean = false
)