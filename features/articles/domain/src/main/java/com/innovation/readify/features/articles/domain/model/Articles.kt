package com.innovation.readify.features.articles.domain.model


data class Articles(
  val articles: List<Article>
)

data class Article(
  val title: String?,
  val imageUrl: String?,
  val description: String?
)
