package com.innovation.readify.features.articles.presentation.articleslist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListEvent
import com.innovation.readify.features.articles.presentation.articleslist.viewmodel.ArticlesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesListScreen(onArticleClicked: (String) -> Unit) {
  val viewModel: ArticlesListViewModel = hiltViewModel()
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Readify Articles") },
      )
    },
  ) { paddingValues ->

    ArticlesListContent(
      modifier = Modifier.padding(paddingValues),
      state = state,
      onLoadMore = {
        viewModel.sendEvent(ArticlesListEvent.LoadMore)
      },
      onRetry = { viewModel.sendEvent(ArticlesListEvent.Retry) },
      onRefresh = { viewModel.sendEvent(ArticlesListEvent.Refresh) },
      onArticleClicked = onArticleClicked,
    )

  }
}

