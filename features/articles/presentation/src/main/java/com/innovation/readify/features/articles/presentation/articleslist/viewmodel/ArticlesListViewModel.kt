package com.innovation.readify.features.articles.presentation.articleslist.viewmodel

import androidx.lifecycle.viewModelScope
import com.innovation.readify.features.articles.domain.usecase.GetArticlesUseCase
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListEffect
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListEvent
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListState
import com.innovation.readify.features.articles.presentation.articleslist.model.toArticlesUiModelList
import com.innovation.readify.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject constructor(private val articlesUseCase: GetArticlesUseCase) :
  BaseViewModel<ArticlesListState, ArticlesListEffect, ArticlesListEvent>(
    initialState = ArticlesListState()
  ) {

  init {
    viewModelScope.launch {
      articlesUseCase(
        1,
        10
      ).onSuccess { updateState { copy(articles = it.articles.toArticlesUiModelList()) } }
        .onFailure { }
    }
  }

  override fun handleEvent(event: ArticlesListEvent) {
    when (event) {
      ArticlesListEvent.LoadMore -> {}
      ArticlesListEvent.Refresh -> {}
      ArticlesListEvent.Retry -> {}
    }
  }
}