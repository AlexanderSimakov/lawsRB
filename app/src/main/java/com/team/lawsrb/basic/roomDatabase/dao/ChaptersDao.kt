package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter

@Dao
interface ChaptersDao {

    @Query("SELECT * FROM ${CodexDatabase.CHAPTERS_NAME} ORDER BY id ASC")
    fun getAll(): List<Chapter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chapter: Chapter)
}