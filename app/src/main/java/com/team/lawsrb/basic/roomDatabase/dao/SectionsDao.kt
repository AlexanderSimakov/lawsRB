package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

@Dao
interface SectionsDao {

    @Query("SELECT * FROM ${CriminalCodexDatabase.SECTIONS_NAME} ORDER BY id ASC")
    fun getAll(): List<Section>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(section: Section)

}