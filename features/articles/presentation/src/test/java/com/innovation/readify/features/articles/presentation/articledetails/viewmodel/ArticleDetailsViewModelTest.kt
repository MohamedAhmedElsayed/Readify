package com.innovation.readify.features.articles.presentation.articledetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.innovation.readify.features.articles.domain.model.Article
import com.innovation.readify.features.articles.domain.usecase.GetArticleByIdUseCase
import com.innovation.readify.features.articles.presentation.MainDispatcherRule
import com.innovation.readify.features.articles.presentation.articledetails.model.ArticleDetailsState
import com.innovation.readify.features.articles.presentation.navigation.ArticlesRoute
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleDetailsViewModelTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var savedStateHandle: SavedStateHandle
  private lateinit var getArticleByIdUseCase: GetArticleByIdUseCase
  private lateinit var viewModel: ArticleDetailsViewModel

  @Before
  fun setup() {
    getArticleByIdUseCase = mockk()
    mockkStatic("androidx.navigation.SavedStateHandleKt")
  }

  @After
  fun tearDown() {
    unmockkStatic("androidx.navigation.SavedStateHandleKt")
  }

  private fun createSavedStateHandleWithRoute(articleId: String): SavedStateHandle {
    val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    every { savedStateHandle.toRoute<ArticlesRoute.ArticleDetails>() } returns ArticlesRoute.ArticleDetails(
      articleId
    )
    return savedStateHandle
  }

  @Test
  fun init_whenArticleExists_shouldUpdateStateWithArticle() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns article

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    advanceUntilIdle()

    // Then
    val state = viewModel.uiState.first()
    assertNotNull(state.articleUiModel)
    assertEquals(articleId, state.articleUiModel?.id)
    assertEquals("Test Article", state.articleUiModel?.title)
    assertEquals("Test Description", state.articleUiModel?.description)
    assertEquals("https://example.com/image.jpg", state.articleUiModel?.imageUrl)

    coVerify(exactly = 1) { getArticleByIdUseCase(articleId) }
  }

  @Test
  fun init_whenArticleDoesNotExist_shouldUpdateStateWithNull() = runTest {
    // Given
    val articleId = "non-existent-id"
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns null

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    advanceUntilIdle()

    // Then
    val state = viewModel.uiState.first()
    assertNull(state.articleUiModel)

    coVerify(exactly = 1) { getArticleByIdUseCase(articleId) }
  }

  @Test
  fun init_whenArticleHasNullFields_shouldMapNullFieldsToEmptyStrings() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = null,
      description = null,
      imageUrl = null
    )
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns article

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    advanceUntilIdle()

    // Then
    val state = viewModel.uiState.first()
    assertNotNull(state.articleUiModel)
    assertEquals(articleId, state.articleUiModel?.id)
    assertEquals("", state.articleUiModel?.title)
    assertEquals("", state.articleUiModel?.description)
    assertEquals("", state.articleUiModel?.imageUrl)
  }

  @Test
  fun init_whenArticleHasPartialNullFields_shouldMapPartialNullFields() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = null,
      imageUrl = "https://example.com/image.jpg"
    )
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns article

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    advanceUntilIdle()

    // Then
    val state = viewModel.uiState.first()
    assertNotNull(state.articleUiModel)
    assertEquals(articleId, state.articleUiModel?.id)
    assertEquals("Test Article", state.articleUiModel?.title)
    assertEquals("", state.articleUiModel?.description)
    assertEquals("https://example.com/image.jpg", state.articleUiModel?.imageUrl)
  }

  @Test
  fun init_shouldExtractArticleIdFromSavedStateHandle() = runTest {
    // Given
    val articleId = "extracted-id-123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns article

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    advanceUntilIdle()

    // Then
    coVerify(exactly = 1) { getArticleByIdUseCase(articleId) }
  }

  @Test
  fun init_shouldInitializeWithEmptyState() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )
    savedStateHandle = createSavedStateHandleWithRoute(articleId)

    coEvery { getArticleByIdUseCase(articleId) } returns article

    // When
    viewModel = ArticleDetailsViewModel(savedStateHandle, getArticleByIdUseCase)
    // Before advanceUntilIdle, state should be initial
    val initialState = viewModel.uiState.first()
    assertEquals(ArticleDetailsState(), initialState)

    advanceUntilIdle()

    // Then
    val finalState = viewModel.uiState.first()
    assertNotNull(finalState.articleUiModel)
  }


}

