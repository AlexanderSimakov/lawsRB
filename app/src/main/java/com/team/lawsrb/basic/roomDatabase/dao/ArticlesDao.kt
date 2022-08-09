package com.team.lawsrb.basic.roomDatabase.dao

import androidx.room.*
import com.team.lawsrb.basic.roomDatabase.CriminalCodexDatabase
import com.team.lawsrb.basic.roomDatabase.codexObjects.Article

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM ${CriminalCodexDatabase.ARTICLES_NAME} ORDER BY id ASC")
    fun getAll(): List<Article>

    @Update
    fun update(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)
}