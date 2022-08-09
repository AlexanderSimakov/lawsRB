package com.team.lawsrb.basic.roomDatabase

import androidx.room.RoomDatabase
import com.team.lawsrb.basic.roomDatabase.dao.ArticlesDao
import com.team.lawsrb.basic.roomDatabase.dao.ChaptersDao
import com.team.lawsrb.basic.roomDatabase.dao.PartsDao
import com.team.lawsrb.basic.roomDatabase.dao.SectionsDao

/**
 * [CodexDatabase] is a parent of all room database classes which contains codex: part, section, chapter and article tables.
 *
 */
abstract class CodexDatabase : RoomDatabase(){
    abstract fun partsDao(): PartsDao
    abstract fun sectionsDao(): SectionsDao
    abstract fun chaptersDao(): ChaptersDao
    abstract fun articlesDao(): ArticlesDao
}