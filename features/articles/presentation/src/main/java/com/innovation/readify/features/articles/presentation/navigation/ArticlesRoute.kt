package com.innovation.readify.features.articles.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ArticlesRoute {
  @Serializable
  data object ArticlesList : ArticlesRoute()

  @Serializable
  data class ArticleDetails(val articleId: String) : ArticlesRoute()
}