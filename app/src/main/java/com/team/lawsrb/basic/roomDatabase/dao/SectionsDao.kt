package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Section

/**
 * [SectionsDao] is a `Dao` interface which give as methods
 * for working with sections database table.
 *
 * @see Section
 */
@Dao
interface SectionsDao {

    /**
     * This method return a [List] of all [Section]s sorted by id.
     */
    @Query("SELECT * FROM ${CodexDatabase.SECTIONS_NAME} ORDER BY id ASC")
    fun getAll(): List<Section>

    /**
     * This method return a [List] of [Section]s whose *id* contained in given [ids].
     */
    @Query("SELECT * FROM ${CodexDatabase.SECTIONS_NAME} WHERE id IN (:ids) ORDER BY id ASC ")
    fun getByIds(ids: List<Int>): List<Section>

    /**
     * This method return a [List] of [Section]s whose *title* contain [search] text,
     * sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.SECTIONS_NAME} WHERE title LIKE :search ORDER BY id ASC")
    fun findAll(search: String): List<Section>

    /**
     * This method insert a [section] into the section table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(section: Section)

    /**
     * This method insert [sections] into the section table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sections: List<Section>)

    /**
     * This method remove all fields from the section table.
     *
     * **Attention:** this changes cannot be undone.
     */
    @Query("DELETE FROM ${CodexDatabase.SECTIONS_NAME}")
    fun clearAll()

}