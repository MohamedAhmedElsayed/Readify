package com.innovation.readify.features.articles.presentation.articleslist.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListEvent
import com.innovation.readify.features.articles.presentation.articleslist.viewmodel.ArticlesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesListScreen(
  onArticleClicked: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ArticlesListViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()



  ArticlesListContent(
    modifier = modifier,
    state = state,
    onLoadMore = {
      viewModel.sendEvent(ArticlesListEvent.LoadMore)
    },
    onRetry = { viewModel.sendEvent(ArticlesListEvent.LoadMore) },
    onRefresh = { viewModel.sendEvent(ArticlesListEvent.Retry) },
    onArticleClicked = onArticleClicked,
  )
}

