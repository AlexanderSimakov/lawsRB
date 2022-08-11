package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.*
import com.team.lawsrb.basic.roomDatabase.CodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} ORDER BY id ASC")
    fun getAll(): List<Article>

    @Query("SELECT * FROM ${CodexDatabase.ARTICLES_NAME} WHERE isLiked = 1 ORDER BY id ASC")
    fun getFavorites(): List<Article>

    @Update
    fun update(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)
}