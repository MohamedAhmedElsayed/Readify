package com.innovation.readify.features.articles.presentation.articleslist.model

data class ArticlesListState(
  val isEndReached: Boolean = false,
  val isLoading: Boolean = false,
  val error: String? = null,
  val articles: List<ArticleUiModel> = emptyList()
)
