package com.innovation.readify.features.articles.domain.repository

import com.innovation.readify.features.articles.domain.model.Article

interface ArticlesRepository {
  suspend fun getTopHeadlines(
    page: Int,
    pageSize: Int,
  ): Result<List<Article>>

  suspend fun getArticleById(id: String): Article?
}