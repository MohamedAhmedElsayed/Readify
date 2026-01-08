package com.innovation.readify.features.articles.data.repository

import com.innovation.readify.features.articles.data.remote.remotedatasource.ArticlesService
import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import javax.inject.Inject

class ArticlesRepositoryImp @Inject constructor(private val service: ArticlesService) :
  ArticlesRepository {
  override suspend fun getTopHeadlines(
    page: Int,
    pageSize: Int
  ) = service.getTopHeadlines(page = page, pageSize = pageSize).map { it.toDomain() }
}