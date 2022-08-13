package com.team.lawsrb.basic.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section
import com.team.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.team.lawsrb.basic.roomDatabase.dao.ChaptersDao
import com.team.lawsrb.basic.roomDatabase.dao.PartsDao
import com.team.lawsrb.basic.roomDatabase.dao.SectionsDao

/**
 * [CodexDatabase] is a parent of all room database classes which contains codex: part, section, chapter and article tables.
 *
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
    abstract fun partsDao(): PartsDao
    abstract fun sectionsDao(): SectionsDao
    abstract fun chaptersDao(): ChaptersDao
    abstract fun articlesDao(): ArticlesDao

    companion object {
        const val PARTS_NAME = "PARTS"
        const val SECTIONS_NAME = "SECTIONS"
        const val CHAPTERS_NAME = "CHAPTERS"
        const val ARTICLES_NAME = "ARTICLES"
    }
}