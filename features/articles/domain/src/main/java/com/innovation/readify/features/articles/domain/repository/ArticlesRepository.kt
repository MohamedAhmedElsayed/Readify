package com.innovation.readify.features.articles.domain.repository

import com.innovation.readify.features.articles.domain.model.Articles

interface ArticlesRepository {
  suspend fun getTopHeadlines(
    page: Int,
    pageSize: Int,
  ): Result<Articles>
}