package com.innovation.readify.features.articles.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class ArticlesResponseModel(
  val articles: List<ArticleModel>?
)

@Serializable
data class ArticleModel(
  val title: String?,
  val urlToImage: String?,
  val description: String?
)
