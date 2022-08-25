package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part

@Dao
interface PartsDao {

    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} ORDER BY id ASC")
    fun getAll(): List<Part>

    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} WHERE id IN (:ids) ORDER BY id ASC ")
    fun getByIds(ids: List<Int>): List<Part>

    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} WHERE title LIKE :search ORDER BY id ASC")
    fun findAll(search: String): List<Part>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(part: Part)

    @Query("DELETE FROM ${CodexDatabase.PARTS_NAME}")
    fun clearAll()

}