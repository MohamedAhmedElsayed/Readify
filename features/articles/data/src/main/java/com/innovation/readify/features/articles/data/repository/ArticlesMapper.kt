package com.innovation.readify.features.articles.data.repository

import com.innovation.readify.features.articles.data.local.entities.ArticlesEntity
import com.innovation.readify.features.articles.data.remote.model.ArticleModel
import com.innovation.readify.features.articles.data.remote.model.ArticlesResponseModel
import com.innovation.readify.features.articles.domain.model.Article
import com.innovation.readify.features.articles.domain.model.Articles

fun ArticlesResponseModel.toDomain() = Articles(
  articles = this.articles?.map { Article(it.title, it.urlToImage, it.description) }.orEmpty()
)

fun ArticlesEntity.toDomain() = Article(
  title = title,
  imageUrl = urlToImage,
  description = description
)

fun ArticleModel.toEntity(page: Int) =
  ArticlesEntity(
    id = uniqueId(),
    title = title,
    description = description,
    urlToImage = urlToImage,
    page = page
  )

private fun ArticleModel.uniqueId(): String {
  val uniqueString = "${title.orEmpty()}_${description.orEmpty()}_${urlToImage.orEmpty()}"
  return uniqueString.hashCode().toString()
}