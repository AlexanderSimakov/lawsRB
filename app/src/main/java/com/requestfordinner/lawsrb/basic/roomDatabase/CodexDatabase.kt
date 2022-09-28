package com.requestfordinner.lawsrb.basic.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Article
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Part
import com.requestfordinner.lawsrb.basic.roomDatabase.codexObjects.Section
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.ChaptersDao
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.PartsDao
import com.requestfordinner.lawsrb.basic.roomDatabase.dao.SectionsDao

/**
 * [CodexDatabase] is a class that provides access to codex tables.
 *
 * @see BaseCodexDatabase
 * @see ArticlesDao
 * @see ChaptersDao
 * @see SectionsDao
 * @see PartsDao
 */
@Database(entities =
[
    Part::class,
    Section::class,
    Chapter::class,
    Article::class
],
    version = 1,
    exportSchema = false)
abstract class CodexDatabase : RoomDatabase(){
    /** This method give access to parts table by return [PartsDao]. */
    abstract fun partsDao(): PartsDao

    /** This method give access to sections table by return [SectionsDao]. */
    abstract fun sectionsDao(): SectionsDao

    /** This method give access to chapters table by return [ChaptersDao]. */
    abstract fun chaptersDao(): ChaptersDao

    /** This method give access to articles table by return [ArticlesDao]. */
    abstract fun articlesDao(): ArticlesDao

    companion object {
        const val PARTS_NAME = "PARTS"
        const val SECTIONS_NAME = "SECTIONS"
        const val CHAPTERS_NAME = "CHAPTERS"
        const val ARTICLES_NAME = "ARTICLES"
    }
}