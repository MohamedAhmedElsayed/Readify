package com.innovation.readify.features.articles.presentation.articledetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.innovation.readify.features.articles.domain.usecase.GetArticleByIdUseCase
import com.innovation.readify.features.articles.presentation.articledetails.model.ArticleDetailsState
import com.innovation.readify.features.articles.presentation.articleslist.model.toUiModel
import com.innovation.readify.features.articles.presentation.navigation.ArticlesRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  getArticleByIdUseCase: GetArticleByIdUseCase
) : ViewModel() {

  private var _uiState = MutableStateFlow(ArticleDetailsState())
  val uiState = _uiState.asStateFlow()

  init {
    val id = savedStateHandle.toRoute<ArticlesRoute.ArticleDetails>().articleId
    viewModelScope.launch {
      val article = getArticleByIdUseCase(id)
      _uiState.value = ArticleDetailsState(articleUiModel = article?.toUiModel())
    }
  }


}