package com.innovation.readify.features.articles.presentation.articleslist.viewmodel

import androidx.lifecycle.viewModelScope
import com.innovation.readify.features.articles.domain.usecase.GetArticlesUseCase
import com.innovation.readify.features.articles.presentation.articleslist.constants.pageSize
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
    loadArticles()
  }

  override fun handleEvent(event: ArticlesListEvent) {
    when (event) {
      ArticlesListEvent.LoadMore -> loadArticles()
      ArticlesListEvent.Refresh -> refresh()
      ArticlesListEvent.Retry -> loadArticles()
    }
  }

  private fun loadArticles() {
    if (state.isLoading || state.isEndReached) return

    updateState { copy(isLoading = true, error = null) }

    viewModelScope.launch {
      articlesUseCase(page = state.currentPage, pageSize = pageSize)
        .onSuccess {
          updateState {
            copy(
              articles = articles + it.toArticlesUiModelList(),
              isLoading = false,
              isEndReached = it.size < pageSize,
              currentPage = currentPage + 1
            )
          }
        }
        .onFailure {
          updateState { copy(isLoading = false, error = it.message) }

        }
    }
  }

  private fun refresh() {
    updateState { ArticlesListState() }
  }
}