package com.innovation.readify.features.articles.domain.model


data class Articles(
  val articles: List<Article>
)

data class Article(
  val id: String?,
  val title: String?,
  val imageUrl: String?,
  val description: String?
)
