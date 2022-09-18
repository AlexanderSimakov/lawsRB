package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Part

/**
 * [PartsDao] is a `Dao` interface which give as methods
 * for working with parts database table.
 *
 * @see Part
 */
@Dao
interface PartsDao {

    /**
     * This method return a [List] of all [Part]s sorted by id.
     */
    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} ORDER BY id ASC")
    fun getAll(): List<Part>

    /**
     * This method return a [List] of [Part]s whose *id* contained in given [ids].
     */
    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} WHERE id IN (:ids) ORDER BY id ASC ")
    fun getByIds(ids: List<Int>): List<Part>

    /**
     * This method return a [List] of [Part]s whose *title* contain [search] text,
     * sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.PARTS_NAME} WHERE title LIKE :search ORDER BY id ASC")
    fun findAll(search: String): List<Part>

    /**
     * This method insert a [part] into the parts table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(part: Part)

    /**
     * This method insert [parts] into the parts table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parts: List<Part>)

    /**
     * This method remove all fields from the parts table.
     *
     * **Attention:** this changes cannot be undone.
     */
    @Query("DELETE FROM ${CodexDatabase.PARTS_NAME}")
    fun clearAll()

}