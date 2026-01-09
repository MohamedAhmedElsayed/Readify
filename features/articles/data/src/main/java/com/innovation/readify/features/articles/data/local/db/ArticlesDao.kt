package com.innovation.readify.features.articles.data.local.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.innovation.readify.features.articles.data.local.entities.ArticlesEntity

@Dao
interface ArticlesDao {
  @Query("SELECT * FROM articles WHERE id = :id")
  suspend fun findArticleById(id: String): ArticlesEntity

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateArticles(articles: List<ArticlesEntity>)

  @Query("SELECT * FROM articles WHERE page = :page")
  suspend fun getArticlesForPage(page: Int): List<ArticlesEntity>
}
