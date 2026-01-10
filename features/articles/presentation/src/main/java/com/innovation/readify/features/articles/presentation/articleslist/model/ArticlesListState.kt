package com.innovation.readify.features.articles.presentation.articleslist.model

data class ArticlesListState(
  val isEndReached: Boolean = false,
  val isLoading: Boolean = false,
  val error: String? = null,
  val currentPage: Int = 1,
  val articles: List<ArticleUiModel> = emptyList()
)
