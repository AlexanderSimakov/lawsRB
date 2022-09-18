package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Chapter

/**
 * [ChaptersDao] is a `Dao` interface which give as methods
 * for working with chapters database table.
 *
 * @see Chapter
 */
@Dao
interface ChaptersDao {

    /**
     * This method return a [List] of all [Chapter]s sorted by id.
     */
    @Query("SELECT * FROM ${CodexDatabase.CHAPTERS_NAME} ORDER BY id ASC")
    fun getAll(): List<Chapter>

    /**
     * This method return a [List] of [Chapter]s whose *id* contained in given [ids].
     */
    @Query("SELECT * FROM ${CodexDatabase.CHAPTERS_NAME} WHERE id IN (:ids) ORDER BY id ASC ")
    fun getByIds(ids: List<Int>): List<Chapter>

    /**
     * This method return a [List] of [Chapter]s whose *title* contain [search] text,
     * sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.CHAPTERS_NAME} WHERE title LIKE :search ORDER BY id ASC")
    fun findAll(search: String): List<Chapter>

    /**
     * This method insert a [chapter] into the chapters table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chapter: Chapter)

    /**
     * This method insert [chapters] into the chapters table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chapters: List<Chapter>)

    /**
     * This method remove all fields from the chapters table.
     *
     * **Attention:** this changes cannot be undone.
     */
    @Query("DELETE FROM ${CodexDatabase.CHAPTERS_NAME}")
    fun clearAll()

}