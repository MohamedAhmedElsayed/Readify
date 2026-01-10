package com.innovation.readify.features.articles.data.repository

import com.innovation.readify.features.articles.data.local.db.ArticlesDao
import com.innovation.readify.features.articles.data.remote.remotedatasource.ArticlesService
import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import javax.inject.Inject

class ArticlesRepositoryImp @Inject constructor(
  private val service: ArticlesService, private val articlesDao: ArticlesDao
) : ArticlesRepository {

  override suspend fun getTopHeadlines(
    page: Int, pageSize: Int
  ) = service.getTopHeadlines(page = page, pageSize = pageSize)
    .fold(
      onSuccess = {
        val articlesEntities = it.articles?.map { it.toEntity(page) }.orEmpty()
        articlesDao.insertOrUpdateArticles(articlesEntities)
        val domainArticles = articlesEntities.map { it.toDomain() }
        Result.success(domainArticles)
      },
      onFailure = {
        val cachedArticles = articlesDao.getArticlesForPage(page).map { it.toDomain() }
        if (cachedArticles.isNotEmpty()) {
          Result.success(cachedArticles)
        } else {
          Result.failure(it)
        }
      })

  override suspend fun getArticleById(id: String) = articlesDao.findArticleById(id).toDomain()
}
