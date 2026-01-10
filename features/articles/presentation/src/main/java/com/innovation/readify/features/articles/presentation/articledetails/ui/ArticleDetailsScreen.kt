package com.innovation.readify.features.articles.presentation.articledetails.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.innovation.readify.features.articles.presentation.articledetails.viewmodel.ArticleDetailsViewModel

@Composable
fun ArticleDetailsScreen(
  onBackClick: () -> Unit,
  articleDetailsViewModel: ArticleDetailsViewModel = hiltViewModel()
) {
  val state by articleDetailsViewModel.uiState.collectAsStateWithLifecycle()
  ArticleDetailsContent(onBackClick = onBackClick, article = state.articleUiModel)
}

