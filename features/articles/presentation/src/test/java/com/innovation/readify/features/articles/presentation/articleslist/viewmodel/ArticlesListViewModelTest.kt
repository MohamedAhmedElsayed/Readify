package com.innovation.readify.features.articles.presentation.articleslist.viewmodel

import app.cash.turbine.test
import com.innovation.readify.features.articles.domain.model.Article
import com.innovation.readify.features.articles.domain.usecase.GetArticlesUseCase
import com.innovation.readify.features.articles.presentation.MainDispatcherRule
import com.innovation.readify.features.articles.presentation.articleslist.constants.pageSize
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListEvent
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticlesListState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesListViewModelTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var getArticlesUseCase: GetArticlesUseCase
  private lateinit var viewModel: ArticlesListViewModel

  @Before
  fun setup() {
    getArticlesUseCase = mockk()
  }

  @Test
  fun init_shouldLoadArticlesOnCreation() = runTest {
    // Given
    val articles = listOf(
      Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"),
      Article(id = "2", title = "Article 2", description = "Desc 2", imageUrl = "url2")
    )

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {

      // Loading state
      val loadingState = awaitItem()
      assertTrue(loadingState.isLoading)
      assertNull(loadingState.error)
      assertEquals(0, loadingState.articles.size)

      // Success state
      val successState = awaitItem()
      assertEquals(2, successState.articles.size)
      assertEquals("Article 1", successState.articles[0].title)
      assertEquals("Article 2", successState.articles[1].title)
      assertFalse(successState.isLoading)
      assertNull(successState.error)
      assertEquals(2, successState.currentPage) // Page incremented after successful load

      cancelAndIgnoreRemainingEvents()
    }

    coVerify(exactly = 1) { getArticlesUseCase(page = 1, pageSize = pageSize) }
  }

  @Test
  fun init_whenLoadFails_shouldSetErrorState() = runTest {
    // Given
    val exception = Exception("Network error")
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.failure(exception)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {

      // Loading state
      val loadingState = awaitItem()
      assertTrue(loadingState.isLoading)
      assertNull(loadingState.error)

      // Error state
      val errorState = awaitItem()
      assertTrue(errorState.articles.isEmpty())
      assertFalse(errorState.isLoading)
      assertEquals("Network error", errorState.error)
      assertEquals(1, errorState.currentPage) // Page not incremented on failure

      cancelAndIgnoreRemainingEvents()
    }

    coVerify(exactly = 1) { getArticlesUseCase(page = 1, pageSize = pageSize) }
  }


  @Test
  fun sendEvent_LoadMore_whenAlreadyLoading_shouldNotLoadAgain() = runTest {
    // Given
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } coAnswers {
      kotlinx.coroutines.delay(100) // Simulate slow loading
      Result.success(emptyList())
    }

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // When - Send LoadMore before initial load completes
    viewModel.sendEvent(ArticlesListEvent.LoadMore)

    viewModel.uiState.test {
      // Skip initial states
      awaitItem() // initial and loading state

      // Then - Should only call use case once (for init)
      awaitItem() // wait for completion
      cancelAndIgnoreRemainingEvents()
    }

    coVerify(exactly = 1) { getArticlesUseCase(any(), any()) }
  }

  @Test
  fun sendEvent_LoadMore_whenEndReached_shouldNotLoadAgain() = runTest {
    // Given - Load less than pageSize to trigger endReached
    val articles =
      listOf(Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"))

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()
      // Success state with endReached = true
      val successState = awaitItem()
      assertTrue(successState.isEndReached)
      assertEquals(1, successState.articles.size)

      // When - LoadMore is called when end is reached
      viewModel.sendEvent(ArticlesListEvent.LoadMore)

      // Then - No new state should be emitted (LoadMore returns early when isEndReached is true)
      // The state remains unchanged, so we verify by checking no new use case calls are made
      cancelAndIgnoreRemainingEvents()
    }

    // Should not make another call
    coVerify(exactly = 1) { getArticlesUseCase(page = 1, pageSize = pageSize) }
    coVerify(exactly = 0) { getArticlesUseCase(page = 2, pageSize = pageSize) }
  }

  @Test
  fun sendEvent_Refresh_shouldResetState() = runTest {
    // Given
    val initialArticles = listOf(
      Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"),
      Article(id = "2", title = "Article 2", description = "Desc 2", imageUrl = "url2")
    )

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(
      initialArticles
    )

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, success)
      skipItems(2)
      // When
      viewModel.sendEvent(ArticlesListEvent.Refresh)

      // Then - Should reset to initial state
      val resetState = awaitItem()
      assertEquals(ArticlesListState(), resetState) // Should be reset to initial state
      assertTrue(resetState.articles.isEmpty())
      assertFalse(resetState.isLoading)
      assertFalse(resetState.isEndReached)
      assertNull(resetState.error)
      assertEquals(1, resetState.currentPage)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun sendEvent_Retry_whenError_shouldRetryLoading() = runTest {
    // Given - Initial load fails
    val exception = Exception("Network error")
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.failure(exception)

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, error)
      skipItems(2)

      // When - Retry after failure
      val articles =
        listOf(Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"))
      coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

      viewModel.sendEvent(ArticlesListEvent.Retry)

      // Loading state
      val loadingState = awaitItem()
      assertTrue(loadingState.isLoading)
      assertNull(loadingState.error)

      // Success state
      val successState = awaitItem()
      assertEquals(1, successState.articles.size)
      assertNull(successState.error)
      assertFalse(successState.isLoading)

      cancelAndIgnoreRemainingEvents()
    }

    coVerify(exactly = 2) { getArticlesUseCase(page = 1, pageSize = pageSize) }
  }

  @Test
  fun loadArticles_whenReturnsLessThanPageSize_shouldSetEndReached() = runTest {
    // Given - Return less articles than pageSize
    val articles = listOf(
      Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1")
    ) // Only 1 article, pageSize is 10

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Success state
      val state = awaitItem()
      assertTrue(state.isEndReached)
      assertEquals(1, state.articles.size)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenReturnsPageSizeArticles_shouldNotSetEndReached() = runTest {
    // Given - Return exactly pageSize articles
    val articles = (1..pageSize).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Success state
      val state = awaitItem()
      assertFalse(state.isEndReached)
      assertEquals(pageSize, state.articles.size)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenReturnsEmptyList_shouldSetEndReached() = runTest {
    // Given
    coEvery {
      getArticlesUseCase(
        page = 1,
        pageSize = pageSize
      )
    } returns Result.success(emptyList())

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Success state
      val state = awaitItem()
      assertTrue(state.isEndReached)
      assertTrue(state.articles.isEmpty())
      assertFalse(state.isLoading)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenLoading_shouldSetLoadingState() = runTest {
    // Given
    coEvery { getArticlesUseCase(page = any(), pageSize = pageSize) } coAnswers {
      kotlinx.coroutines.delay(50)
      Result.success(emptyList())
    }

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then - Check loading state
    viewModel.uiState.test {

      // Loading state
      val loadingState = awaitItem()
      println(loadingState)
      assertTrue(loadingState.isLoading)

      // Final state after completion
      val finalState = awaitItem()
      assertFalse(finalState.isLoading) // Should be false after completion

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenSuccess_shouldClearError() = runTest {
    // Given - First load fails, then retry succeeds
    val exception = Exception("Network error")
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.failure(exception)

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, error)
      awaitItem()

      // Verify error state
      val errorState = awaitItem()
      assertNotNull(errorState.error)

      // When - Retry with success
      val articles =
        listOf(Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"))
      coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

      viewModel.sendEvent(ArticlesListEvent.Retry)

      // Loading state (error should be cleared)
      val loadingState = awaitItem()
      assertNull(loadingState.error)
      assertTrue(loadingState.isLoading)

      // Success state
      val successState = awaitItem()
      assertNull(successState.error)
      assertEquals(1, successState.articles.size)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenMultiplePages_shouldAppendArticles() = runTest {
    // Given
    val page1Articles = (1..pageSize).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }
    val page2Articles = ((pageSize + 1)..(pageSize * 2)).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(
      page1Articles
    )
    coEvery { getArticlesUseCase(page = 2, pageSize = pageSize) } returns Result.success(
      page2Articles
    )

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, first success)
      skipItems(2)
      // When - Load more
      viewModel.sendEvent(ArticlesListEvent.LoadMore)

      // Loading state
      val loadingState = awaitItem()
      assertTrue(loadingState.isLoading)
      assertEquals(pageSize, loadingState.articles.size)

      // Success state with appended articles
      val successState = awaitItem()
      assertEquals(pageSize * 2, successState.articles.size)
      assertEquals("Article 1", successState.articles[0].title)
      assertEquals("Article ${pageSize + 1}", successState.articles[pageSize].title)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenFailure_shouldNotIncrementPage() = runTest {
    // Given
    val exception = Exception("Network error")
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.failure(exception)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Error state
      val state = awaitItem()
      assertEquals(1, state.currentPage)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenSuccess_shouldIncrementPage() = runTest {
    // Given
    val articles = (1..pageSize).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Success state
      val state = awaitItem()
      assertEquals(2, state.currentPage)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenLoading_shouldSetErrorToNull() = runTest {
    // Given - Previous state has error
    val exception = Exception("Network error")
    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.failure(exception)

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, error)
      awaitItem()

      // Verify error state
      val errorState = awaitItem()
      assertNotNull(errorState.error)

      // When - Retry (this will clear error and start loading)
      val articles =
        listOf(Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"))
      coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

      viewModel.sendEvent(ArticlesListEvent.Retry)

      // Loading state (error should be cleared)
      val loadingState = awaitItem()
      assertNull(loadingState.error)
      assertTrue(loadingState.isLoading)

      // Success state
      val successState = awaitItem()
      assertNull(successState.error)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun handleEvent_Refresh_shouldResetToInitialState() = runTest {
    // Given
    val articles = listOf(
      Article(id = "1", title = "Article 1", description = "Desc 1", imageUrl = "url1"),
      Article(id = "2", title = "Article 2", description = "Desc 2", imageUrl = "url2")
    )

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, success)
      awaitItem()

      // Verify initial success state
      val successState = awaitItem()
      assertEquals(2, successState.articles.size)
      assertEquals(2, successState.currentPage)

      // When
      viewModel.sendEvent(ArticlesListEvent.Refresh)

      // Then - Should reset to initial state
      val resetState = awaitItem()
      assertEquals(ArticlesListState(), resetState)
      assertTrue(resetState.articles.isEmpty())
      assertEquals(1, resetState.currentPage)
      assertFalse(resetState.isEndReached)
      assertFalse(resetState.isLoading)
      assertNull(resetState.error)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenArticlesHaveNullFields_shouldMapToEmptyStrings() = runTest {
    // Given
    val articles = listOf(
      Article(id = "1", title = null, description = null, imageUrl = null)
    )

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(articles)

    // When
    viewModel = ArticlesListViewModel(getArticlesUseCase)

    // Then
    viewModel.uiState.test {
      // Skip initial states (initial, loading)
      awaitItem()

      // Success state
      val state = awaitItem()
      assertEquals(1, state.articles.size)
      assertEquals("", state.articles[0].title)
      assertEquals("", state.articles[0].description)
      assertEquals("", state.articles[0].imageUrl)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun loadArticles_whenMultipleLoadMoreCalls_shouldLoadSequentially() = runTest {
    // Given
    val page1Articles = (1..pageSize).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }
    val page2Articles = ((pageSize + 1)..(pageSize * 2)).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }
    val page3Articles = ((pageSize * 2 + 1)..(pageSize * 3)).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Desc $index",
        imageUrl = "url$index"
      )
    }

    coEvery { getArticlesUseCase(page = 1, pageSize = pageSize) } returns Result.success(
      page1Articles
    )
    coEvery { getArticlesUseCase(page = 2, pageSize = pageSize) } returns Result.success(
      page2Articles
    )
    coEvery { getArticlesUseCase(page = 3, pageSize = pageSize) } returns Result.success(
      page3Articles
    )

    viewModel = ArticlesListViewModel(getArticlesUseCase)

    viewModel.uiState.test {
      // Skip initial states (initial, loading, first success)
      skipItems(2)

      // When - Load more twice
      viewModel.sendEvent(ArticlesListEvent.LoadMore)
      // Skip loading state
      skipItems(1)
      // Second page success
      val page2State = awaitItem()
      assertEquals(pageSize * 2, page2State.articles.size)
      assertEquals(3, page2State.currentPage)

      viewModel.sendEvent(ArticlesListEvent.LoadMore)
      // Skip loading state
      skipItems(1)
      // Third page success
      val page3State = awaitItem()
      assertEquals(pageSize * 3, page3State.articles.size)
      assertEquals(4, page3State.currentPage)

      cancelAndIgnoreRemainingEvents()
    }

    coVerify(exactly = 1) { getArticlesUseCase(page = 1, pageSize = pageSize) }
    coVerify(exactly = 1) { getArticlesUseCase(page = 2, pageSize = pageSize) }
    coVerify(exactly = 1) { getArticlesUseCase(page = 3, pageSize = pageSize) }
  }
}

