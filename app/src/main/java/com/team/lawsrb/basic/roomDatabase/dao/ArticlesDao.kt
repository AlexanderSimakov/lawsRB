package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.*
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article

/**
 * [ArticlesDao] is a `Dao` interface which give as methods
 * for working with articles database table.
 *
 * @see Article
 */
@Dao
interface ArticlesDao {

    /**
     * This method return a [List] of all [Article]s sorted by id.
     */
    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} ORDER BY id ASC")
    suspend fun getAll(): List<Article>

    /**
     * This method return a [List] of all favorites [Article]s sorted by id.
     */
    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} WHERE isLiked = 1 ORDER BY id ASC")
    suspend fun getFavorites(): List<Article>

    /**
     * This method return a [List] of [Article]s whose *title* or *content*
     * contain [search] text, sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} WHERE title LIKE :search OR content LIKE :search ORDER BY id ASC")
    suspend fun findAll(search: String): List<Article>

    /**
     * This method return a [List] of [Article]s whose *title* contain [search] text,
     * sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} WHERE title LIKE :search ORDER BY id ASC")
    suspend fun findByTitle(search: String): List<Article>

    /**
     * This method return a [List] of [Article]s whose *content* contain [search] text,
     * sorted by *id*.
     */
    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} WHERE content LIKE :search ORDER BY id ASC")
    suspend fun findByContent(search: String): List<Article>

    /**
     * This method replace existing [Article] with [article], found by *id*.
     */
    @Update
    suspend fun update(article: Article)

    /**
     * This method insert a [Article] into the articles table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    /**
     * This method insert [articles] into the articles table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<Article>)

    /**
     * This method remove all fields from the articles table.
     *
     * **Attention:** this changes cannot be undone.
     */
    @Query("DELETE FROM ${CodexDatabase.ARTICLES_NAME}")
    suspend fun clearAll()

}