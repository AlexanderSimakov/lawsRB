package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part

@Dao
interface PartsDao {

    @Query("SELECT * FROM ${CriminalCodexDatabase.PARTS_NAME} ORDER BY id ASC")
    fun getAll(): List<Part>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(part: Part)

}