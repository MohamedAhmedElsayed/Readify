package com.innovation.readify.features.articles.presentation.articleslist.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.innovation.readify.designsystem.componentts.ErrorFullScreen
import com.innovation.readify.designsystem.componentts.LoadingItem
import com.innovation.readify.designsystem.componentts.PullToRefreshIndicator
import com.innovation.readify.designsystem.theme.LocalSpacing
import com.innovation.readify.designsystem.theme.ReadifyTheme
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticleUiModel
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesListContent(
  state: ArticlesListState,
  onLoadMore: () -> Unit,
  onRetry: () -> Unit,
  onRefresh: () -> Unit,
  onArticleClicked: (String) -> Unit,
  modifier: Modifier
) {
  val spacing = LocalSpacing.current
  val lazyListState = rememberLazyListState()
  val isRefreshing = state.isLoading && state.articles.isEmpty()
  val pullToRefreshState = rememberPullToRefreshState()

  val shouldLoadMore = remember {
    derivedStateOf {
      val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
      val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
      if (totalItemsCount == 0) return@derivedStateOf false
      val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: -1
      lastVisibleItemIndex >= totalItemsCount - 1
    }
  }

  LaunchedEffect(lazyListState, state.isLoading, state.isEndReached) {
    snapshotFlow { shouldLoadMore.value }
      .collect { trigger ->
        if (trigger && !state.isLoading && !state.isEndReached && state.error == null) {
          onLoadMore()
        }
      }
  }

  PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = onRefresh,
    modifier = modifier,
    state = pullToRefreshState,
    indicator = {
      if (state.articles.isEmpty().not())
        PullToRefreshIndicator(
          state = pullToRefreshState,
          isRefreshing = isRefreshing,
          modifier = Modifier.align(Alignment.TopCenter)
        )
    }
  ) {
    LazyColumn(
      state = lazyListState,
      contentPadding = PaddingValues(spacing.m),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(state.articles) { article ->
        ArticleItem(
          article = article,
          onArticleClick = onArticleClicked,
        )
        Spacer(modifier = Modifier.height(spacing.xs))
      }
      item {
        when {
          state.isLoading && state.articles.isNotEmpty() -> LoadingItem()
          state.error != null && state.articles.isNotEmpty() -> RetryLoading(onRetry)
          state.isEndReached -> EndReachedMessage()
          state.isLoading.not() && state.articles.isEmpty() && state.error == null -> EmptyScreenWithRetry(
            onRetry
          )
        }
      }
    }
  }

  if (state.error != null && state.articles.isEmpty()) {
    ErrorFullScreen(onRetry)
  }
}

@Preview(showBackground = true, name = "Articles List - With Articles")
@Composable
fun ArticlesListContentPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = listOf(
          ArticleUiModel(
            id = "1",
            title = "Breaking News: Technology Advances in 2024",
            description = "Latest developments in technology and innovation",
            imageUrl = "https://picsum.photos/400/200?random=1"
          ),
          ArticleUiModel(
            id = "1",
            title = "Climate Change Summit Reaches Historic Agreement",
            description = "World leaders agree on new climate targets",
            imageUrl = "https://picsum.photos/400/200?random=2"
          ),
          ArticleUiModel(
            id = "1",
            title = "Space Exploration: New Mission to Mars",
            description = "NASA announces ambitious new mission",
            imageUrl = "https://picsum.photos/400/200?random=3"
          ),
          ArticleUiModel(
            id = "1",
            title = "Healthcare Breakthrough: New Treatment Discovered",
            description = "Scientists develop revolutionary medical treatment",
            imageUrl = "https://picsum.photos/400/200?random=4"
          ),
          ArticleUiModel(
            id = "1",
            title = "Sports: Championship Finals This Weekend",
            description = "Exciting matchups in the championship finals",
            imageUrl = "https://picsum.photos/400/200?random=5"
          )
        ),
        isLoading = false,
        isEndReached = false,
        error = null
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Articles List - Loading")
@Composable
fun ArticlesListContentLoadingPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = listOf(
          ArticleUiModel(
            id = "1",
            title = "Sample Article Title",
            description = "Sample description",
            imageUrl = "https://picsum.photos/400/200?random=1"
          )
        ),
        isLoading = true,
        isEndReached = false,
        error = null
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Articles List - Error with Articles")
@Composable
fun ArticlesListContentErrorWithArticlesPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = listOf(
          ArticleUiModel(
            id = "1",
            title = "Sample Article Title",
            description = "Sample description",
            imageUrl = "https://picsum.photos/400/200?random=1"
          )
        ),
        isLoading = false,
        isEndReached = false,
        error = "Failed to load more articles"
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Articles List - End Reached")
@Composable
fun ArticlesListContentEndReachedPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = listOf(
          ArticleUiModel(
            id = "1",
            title = "Sample Article Title",
            description = "Sample description",
            imageUrl = "https://picsum.photos/400/200?random=1"
          )
        ),
        isLoading = false,
        isEndReached = true,
        error = null
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Articles List - Empty")
@Composable
fun ArticlesListContentEmptyPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = emptyList(),
        isLoading = false,
        isEndReached = false,
        error = null
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Articles List - Full Error Screen")
@Composable
fun ArticlesListContentFullErrorPreview() {
  ReadifyTheme {
    ArticlesListContent(
      state = ArticlesListState(
        articles = emptyList(),
        isLoading = false,
        isEndReached = false,
        error = "Network error occurred"
      ),
      onLoadMore = {},
      onRetry = {},
      onRefresh = {},
      onArticleClicked = {},
      modifier = Modifier
    )
  }
}

