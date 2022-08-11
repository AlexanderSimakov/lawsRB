package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

@Dao
interface SectionsDao {

    @Query("SELECT * FROM ${CodexDatabase.SECTIONS_NAME} ORDER BY id ASC")
    fun getAll(): List<Section>

    @Query("SELECT * FROM ${CodexDatabase.SECTIONS_NAME} WHERE id IN (:ids) ORDER BY id ASC ")
    fun getByIds(ids: List<Int>): List<Section>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(section: Section)

}