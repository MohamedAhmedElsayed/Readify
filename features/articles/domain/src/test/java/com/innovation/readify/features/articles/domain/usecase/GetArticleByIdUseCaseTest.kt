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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetArticleByIdUseCaseTest {

  private lateinit var repository: ArticlesRepository
  private lateinit var useCase: GetArticleByIdUseCase

  @Before
  fun setup() {
    repository = mockk()
    useCase = GetArticleByIdUseCase(repository)
  }

  @Test
  fun invoke_whenArticleExists_shouldReturnArticle() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    val result = useCase(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result?.id)
    assertEquals("Test Article", result?.title)
    assertEquals("Test Description", result?.description)
    assertEquals("https://example.com/image.jpg", result?.imageUrl)

    coVerify(exactly = 1) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenArticleDoesNotExist_shouldReturnNull() = runTest {
    // Given
    val articleId = "non-existent-id"

    coEvery { repository.getArticleById(articleId) } returns null

    // When
    val result = useCase(articleId)

    // Then
    assertNull(result)

    coVerify(exactly = 1) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenArticleHasNullFields_shouldReturnArticleWithNullFields() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = null,
      description = null,
      imageUrl = null
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    val result = useCase(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result?.id)
    assertNull(result?.title)
    assertNull(result?.description)
    assertNull(result?.imageUrl)
  }

  @Test
  fun invoke_whenArticleHasPartialNullFields_shouldReturnArticleWithPartialNullFields() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = null,
      imageUrl = "https://example.com/image.jpg"
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    val result = useCase(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result?.id)
    assertEquals("Test Article", result?.title)
    assertNull(result?.description)
    assertEquals("https://example.com/image.jpg", result?.imageUrl)
  }

  @Test
  fun invoke_shouldPassCorrectArticleIdToRepository() = runTest {
    // Given
    val articleId = "specific-id-123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    useCase(articleId)

    // Then
    coVerify(exactly = 1) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenCalledWithDifferentIds_shouldCallRepositoryWithCorrectId() = runTest {
    // Given
    val articleId1 = "id-1"
    val articleId2 = "id-2"
    val article1 = Article(
      id = articleId1,
      title = "Article 1",
      description = "Description 1",
      imageUrl = "https://example.com/image1.jpg"
    )
    val article2 = Article(
      id = articleId2,
      title = "Article 2",
      description = "Description 2",
      imageUrl = "https://example.com/image2.jpg"
    )

    coEvery { repository.getArticleById(articleId1) } returns article1
    coEvery { repository.getArticleById(articleId2) } returns article2

    // When
    val result1 = useCase(articleId1)
    val result2 = useCase(articleId2)

    // Then
    assertNotNull(result1)
    assertEquals(articleId1, result1?.id)
    assertEquals("Article 1", result1?.title)

    assertNotNull(result2)
    assertEquals(articleId2, result2?.id)
    assertEquals("Article 2", result2?.title)

    coVerify(exactly = 1) { repository.getArticleById(articleId1) }
    coVerify(exactly = 1) { repository.getArticleById(articleId2) }
  }

  @Test
  fun invoke_whenArticleIdIsEmptyString_shouldCallRepositoryWithEmptyString() = runTest {
    // Given
    val articleId = ""

    coEvery { repository.getArticleById(articleId) } returns null

    // When
    val result = useCase(articleId)

    // Then
    assertNull(result)
    coVerify(exactly = 1) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenArticleIdIsLongString_shouldCallRepositoryWithLongString() = runTest {
    // Given
    val articleId = "a".repeat(1000)
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    val result = useCase(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result?.id)
    coVerify(exactly = 1) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenArticleIdContainsSpecialCharacters_shouldCallRepositoryWithSpecialCharacters() =
    runTest {
      // Given
      val articleId = "id-123_@#$%^&*()"
      val article = Article(
        id = articleId,
        title = "Test Article",
        description = "Test Description",
        imageUrl = "https://example.com/image.jpg"
      )

      coEvery { repository.getArticleById(articleId) } returns article

      // When
      val result = useCase(articleId)

      // Then
      assertNotNull(result)
      assertEquals(articleId, result?.id)
      coVerify(exactly = 1) { repository.getArticleById(articleId) }
    }

  @Test
  fun invoke_whenRepositoryThrowsException_shouldPropagateException() = runTest {
    // Given
    val articleId = "123"
    val exception = RuntimeException("Repository error")

    coEvery { repository.getArticleById(articleId) } throws exception

    // When & Then
    try {
      useCase(articleId)
      assertTrue("Expected exception to be thrown", false)
    } catch (e: RuntimeException) {
      assertEquals("Repository error", e.message)
    }
  }

  @Test
  fun invoke_whenCalledMultipleTimesWithSameId_shouldCallRepositoryEachTime() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      imageUrl = "https://example.com/image.jpg"
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    useCase(articleId)
    useCase(articleId)
    useCase(articleId)

    // Then
    coVerify(exactly = 3) { repository.getArticleById(articleId) }
  }

  @Test
  fun invoke_whenArticleHasEmptyStringFields_shouldReturnArticleWithEmptyStrings() = runTest {
    // Given
    val articleId = "123"
    val article = Article(
      id = articleId,
      title = "",
      description = "",
      imageUrl = ""
    )

    coEvery { repository.getArticleById(articleId) } returns article

    // When
    val result = useCase(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result?.id)
    assertEquals("", result?.title)
    assertEquals("", result?.description)
    assertEquals("", result?.imageUrl)
  }
}

