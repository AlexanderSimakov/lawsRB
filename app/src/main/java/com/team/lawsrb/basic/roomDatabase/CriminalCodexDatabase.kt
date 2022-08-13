package com.team.lawsrb.basic.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

@Database(entities =
    [
        Part::class,
        Section::class,
        Chapter::class,
        Article::class
    ],
    version = 1,
    exportSchema = false)
abstract class CriminalCodexDatabase: CodexDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: CriminalCodexDatabase? = null
        private const val DATABASE_NAME = "codex_database"
        // TODO change DATABASE_ASSETS_PATH to actual
        private const val DATABASE_ASSETS_PATH = "database/codex_database"

        fun getInstance(context: Context? = null): CriminalCodexDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            if (context == null){
                // TODO Make error message better.
                throw IllegalArgumentException("Trying to get database instance without initializing")
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CriminalCodexDatabase::class.java,
                    DATABASE_NAME
                ).createFromAsset(DATABASE_ASSETS_PATH)
                 .fallbackToDestructiveMigration()
                 .allowMainThreadQueries()
                 .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}