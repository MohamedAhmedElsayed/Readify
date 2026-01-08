package com.innovation.readify.features.articles.data.model

import kotlinx.serialization.Serializable


@Serializable
data class ArticlesModel(
  val articles: List<Article>?
)

@Serializable
data class Article(
  val title: String?,
  val urlToImage: String?,
  val description: String?
)
