package com.innovation.readify.features.articles.presentation.articleslist.model

sealed class ArticlesListEvent {
  data object LoadMore : ArticlesListEvent()
  data object Refresh : ArticlesListEvent()
  data object Retry : ArticlesListEvent()
}