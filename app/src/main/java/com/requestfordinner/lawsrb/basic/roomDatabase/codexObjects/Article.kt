package com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.requestfordinner.lawsrb.basic.roomDatabase.CodexDatabase

/**
 * A *data class* that represents single codex article.
 *
 * @property title Name of an [Article].
 * @property id Unique id.
 * @property parentId Id of an [Article] parent.
 * @property isLiked Equals *true* if [Article] marked as favorite.
 * @property content Contain an [Article] items as a [String].
 *
 * @constructor Creates single [Article] object.
 */
@Entity(
    tableName = CodexDatabase.ARTICLES_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["id"],
            childColumns = ["chapter_id"]
        )
    ]
)
data class Article(
    override val title: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "chapter_id") val parentId: Int,
    var isLiked: Boolean = false,
    var content: String = ""
) : TitledItem
