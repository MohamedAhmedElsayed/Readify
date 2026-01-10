package com.innovation.readify.features.articles.domain.usecase

import com.innovation.readify.features.articles.domain.model.Article
import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetArticlesUseCaseTest {

  private lateinit var repository: ArticlesRepository
  private lateinit var useCase: GetArticlesUseCase

  @Before
  fun setup() {
    repository = mockk()
    useCase = GetArticlesUseCase(repository)
  }

  @Test
  fun invoke_whenRepositoryReturnsSuccess_shouldReturnSuccessResult() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val articles = listOf(
      Article(
        id = "1",
        title = "Article 1",
        description = "Description 1",
        imageUrl = "https://example.com/image1.jpg"
      ),
      Article(
        id = "2",
        title = "Article 2",
        description = "Description 2",
        imageUrl = "https://example.com/image2.jpg"
      )
    )

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(articles)

    // When
    val result = useCase(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val resultArticles = result.getOrNull()
    assertNotNull(resultArticles)
    assertEquals(2, resultArticles?.size)
    assertEquals("Article 1", resultArticles?.first()?.title)
    assertEquals("Article 2", resultArticles?.last()?.title)

    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_whenRepositoryReturnsFailure_shouldReturnFailureResult() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = Exception("Network error")

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.failure(exception)

    // When
    val result = useCase(page, pageSize)

    // Then
    assertTrue(result.isFailure)
    assertEquals(exception, result.exceptionOrNull())

    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_whenRepositoryReturnsEmptyList_shouldReturnSuccessWithEmptyList() = runTest {
    // Given
    val page = 1
    val pageSize = 10

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(emptyList())

    // When
    val result = useCase(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val resultArticles = result.getOrNull()
    assertNotNull(resultArticles)
    assertTrue(resultArticles?.isEmpty() == true)

    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_shouldPassCorrectParametersToRepository() = runTest {
    // Given
    val page = 2
    val pageSize = 20
    val articles = listOf(
      Article(
        id = "1",
        title = "Article 1",
        description = "Description 1",
        imageUrl = "https://example.com/image1.jpg"
      )
    )

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(articles)

    // When
    useCase(page, pageSize)

    // Then
    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_whenRepositoryReturnsArticlesWithNullFields_shouldReturnArticlesWithNullFields() =
    runTest {
      // Given
      val page = 1
      val pageSize = 10
      val articles = listOf(
        Article(
          id = "1",
          title = null,
          description = null,
          imageUrl = null
        )
      )

      coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(articles)

      // When
      val result = useCase(page, pageSize)

      // Then
      assertTrue(result.isSuccess)
      val resultArticles = result.getOrNull()
      assertNotNull(resultArticles)
      assertEquals(1, resultArticles?.size)
      assertTrue(resultArticles?.first()?.title == null)
      assertTrue(resultArticles?.first()?.description == null)
      assertTrue(resultArticles?.first()?.imageUrl == null)
    }

  @Test
  fun invoke_whenCalledMultipleTimes_shouldCallRepositoryEachTime() = runTest {
    // Given
    val page1 = 1
    val page2 = 2
    val pageSize = 10
    val articles1 = listOf(
      Article(
        id = "1",
        title = "Article 1",
        description = "Description 1",
        imageUrl = "https://example.com/image1.jpg"
      )
    )
    val articles2 = listOf(
      Article(
        id = "2",
        title = "Article 2",
        description = "Description 2",
        imageUrl = "https://example.com/image2.jpg"
      )
    )

    coEvery { repository.getTopHeadlines(page1, pageSize) } returns Result.success(articles1)
    coEvery { repository.getTopHeadlines(page2, pageSize) } returns Result.success(articles2)

    // When
    useCase(page1, pageSize)
    useCase(page2, pageSize)

    // Then
    coVerify(exactly = 1) { repository.getTopHeadlines(page1, pageSize) }
    coVerify(exactly = 1) { repository.getTopHeadlines(page2, pageSize) }
  }

  @Test
  fun invoke_whenRepositoryReturnsLargeList_shouldReturnAllArticles() = runTest {
    // Given
    val page = 1
    val pageSize = 100
    val articles = (1..100).map { index ->
      Article(
        id = index.toString(),
        title = "Article $index",
        description = "Description $index",
        imageUrl = "https://example.com/image$index.jpg"
      )
    }

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(articles)

    // When
    val result = useCase(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val resultArticles = result.getOrNull()
    assertNotNull(resultArticles)
    assertEquals(100, resultArticles?.size)
    assertEquals("Article 1", resultArticles?.first()?.title)
    assertEquals("Article 100", resultArticles?.last()?.title)
  }

  @Test
  fun invoke_whenPageIsZero_shouldPassZeroToRepository() = runTest {
    // Given
    val page = 0
    val pageSize = 10

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(emptyList())

    // When
    useCase(page, pageSize)

    // Then
    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_whenPageSizeIsOne_shouldPassOneToRepository() = runTest {
    // Given
    val page = 1
    val pageSize = 1
    val articles = listOf(
      Article(
        id = "1",
        title = "Article 1",
        description = "Description 1",
        imageUrl = "https://example.com/image1.jpg"
      )
    )

    coEvery { repository.getTopHeadlines(page, pageSize) } returns Result.success(articles)

    // When
    val result = useCase(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    assertEquals(1, result.getOrNull()?.size)
    coVerify(exactly = 1) { repository.getTopHeadlines(page, pageSize) }
  }

  @Test
  fun invoke_whenRepositoryThrowsException_shouldPropagateException() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = RuntimeException("Repository error")

    coEvery { repository.getTopHeadlines(page, pageSize) } throws exception

    // When & Then
    try {
      useCase(page, pageSize)
      assertTrue("Expected exception to be thrown", false)
    } catch (e: RuntimeException) {
      assertEquals("Repository error", e.message)
    }
  }
}

