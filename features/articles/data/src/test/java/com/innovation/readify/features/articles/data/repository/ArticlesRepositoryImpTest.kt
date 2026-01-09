package com.innovation.readify.features.articles.data.repository

import com.innovation.readify.features.articles.data.MainDispatcherRule
import com.innovation.readify.features.articles.data.local.db.ArticlesDao
import com.innovation.readify.features.articles.data.local.entities.ArticlesEntity
import com.innovation.readify.features.articles.data.remote.model.ArticleModel
import com.innovation.readify.features.articles.data.remote.model.ArticlesResponseModel
import com.innovation.readify.features.articles.data.remote.remotedatasource.ArticlesService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesRepositoryImpTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var service: ArticlesService
  private lateinit var articlesDao: ArticlesDao
  private lateinit var repository: ArticlesRepositoryImp

  @Before
  fun setup() {
    service = mockk()
    articlesDao = mockk()
    repository = ArticlesRepositoryImp(service, articlesDao)
  }

  @Test
  fun getTopHeadlines_whenServiceReturnsSuccess_shouldCacheAndReturnDomainArticles() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val articleModel1 = ArticleModel(
      title = "Test Article 1",
      description = "Description 1",
      urlToImage = "https://example.com/image1.jpg"
    )
    val articleModel2 = ArticleModel(
      title = "Test Article 2",
      description = "Description 2",
      urlToImage = "https://example.com/image2.jpg"
    )
    val responseModel = ArticlesResponseModel(articles = listOf(articleModel1, articleModel2))

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val articles = result.getOrNull()
    assertNotNull(articles)
    assertEquals(2, articles?.size)

    // Verify articles are mapped correctly
    val article1 = articles?.find { it.title == "Test Article 1" }
    assertNotNull(article1)
    assertEquals("Description 1", article1?.description)
    assertEquals("https://example.com/image1.jpg", article1?.imageUrl)

    val article2 = articles?.find { it.title == "Test Article 2" }
    assertNotNull(article2)
    assertEquals("Description 2", article2?.description)
    assertEquals("https://example.com/image2.jpg", article2?.imageUrl)

    // Verify caching
    coVerify(exactly = 1) { articlesDao.insertOrUpdateArticles(any()) }
  }

  @Test
  fun getTopHeadlines_whenServiceReturnsSuccessWithNullArticles_shouldReturnEmptyList() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val responseModel = ArticlesResponseModel(articles = null)

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val articles = result.getOrNull()
    assertNotNull(articles)
    assertTrue(articles?.isEmpty() == true)

    // Verify caching was called with empty list
    coVerify(exactly = 1) { articlesDao.insertOrUpdateArticles(emptyList()) }
  }

  @Test
  fun getTopHeadlines_whenServiceReturnsSuccessWithEmptyArticles_shouldReturnEmptyList() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val responseModel = ArticlesResponseModel(articles = emptyList())

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val articles = result.getOrNull()
    assertNotNull(articles)
    assertTrue(articles?.isEmpty() == true)

    // Verify caching was called with empty list
    coVerify(exactly = 1) { articlesDao.insertOrUpdateArticles(emptyList()) }
  }

  @Test
  fun getTopHeadlines_whenServiceFailsAndCacheHasData_shouldReturnCachedArticles() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = Exception("Network error")
    val cachedEntity1 = ArticlesEntity(
      id = "1",
      title = "Cached Article 1",
      description = "Cached Description 1",
      urlToImage = "https://example.com/cached1.jpg",
      page = page
    )
    val cachedEntity2 = ArticlesEntity(
      id = "2",
      title = "Cached Article 2",
      description = "Cached Description 2",
      urlToImage = "https://example.com/cached2.jpg",
      page = page
    )

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.failure(
      exception
    )
    coEvery { articlesDao.getArticlesForPage(page) } returns listOf(cachedEntity1, cachedEntity2)

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val articles = result.getOrNull()
    assertNotNull(articles)
    assertEquals(2, articles?.size)

    // Verify cached articles are returned
    val article1 = articles?.find { it.id == "1" }
    assertNotNull(article1)
    assertEquals("Cached Article 1", article1?.title)
    assertEquals("Cached Description 1", article1?.description)
    assertEquals("https://example.com/cached1.jpg", article1?.imageUrl)

    // Verify service was called
    coVerify(exactly = 1) { service.getTopHeadlines(page = page, pageSize = pageSize) }
    // Verify cache was queried
    coVerify(exactly = 1) { articlesDao.getArticlesForPage(page) }
    // Verify no insert was called
    coVerify(exactly = 0) { articlesDao.insertOrUpdateArticles(any()) }
  }

  @Test
  fun getTopHeadlines_whenServiceFailsAndCacheIsEmpty_shouldReturnFailure() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = Exception("Network error")

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.failure(
      exception
    )
    coEvery { articlesDao.getArticlesForPage(page) } returns emptyList()

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isFailure)
    assertEquals(exception, result.exceptionOrNull())

    // Verify service was called
    coVerify(exactly = 1) { service.getTopHeadlines(page = page, pageSize = pageSize) }
    // Verify cache was queried
    coVerify(exactly = 1) { articlesDao.getArticlesForPage(page) }
    // Verify no insert was called
    coVerify(exactly = 0) { articlesDao.insertOrUpdateArticles(any()) }
  }

  @Test
  fun getTopHeadlines_whenServiceFailsAndCacheIsNull_shouldReturnFailure() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = Exception("Network error")

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.failure(
      exception
    )
    coEvery { articlesDao.getArticlesForPage(page) } returns emptyList()

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isFailure)
    assertEquals(exception, result.exceptionOrNull())
  }

  @Test
  fun getTopHeadlines_shouldMapArticlesWithCorrectPageNumber() = runTest {
    // Given
    val page = 2
    val pageSize = 20
    val articleModel = ArticleModel(
      title = "Test Article",
      description = "Description",
      urlToImage = "https://example.com/image.jpg"
    )
    val responseModel = ArticlesResponseModel(articles = listOf(articleModel))

    val capturedEntities = slot<List<ArticlesEntity>>()
    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery {
      articlesDao.insertOrUpdateArticles(capture(capturedEntities))
    } returns Unit

    // When
    repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(capturedEntities.isCaptured)
    assertEquals(1, capturedEntities.captured.size)
    assertEquals(page, capturedEntities.captured.first().page)
  }

  @Test
  fun getArticleById_whenArticleExists_shouldReturnDomainArticle() = runTest {
    // Given
    val articleId = "123"
    val entity = ArticlesEntity(
      id = articleId,
      title = "Test Article",
      description = "Test Description",
      urlToImage = "https://example.com/image.jpg",
      page = 1
    )

    coEvery { articlesDao.findArticleById(articleId) } returns entity

    // When
    val result = repository.getArticleById(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result.id)
    assertEquals("Test Article", result.title)
    assertEquals("Test Description", result.description)
    assertEquals("https://example.com/image.jpg", result.imageUrl)

    coVerify(exactly = 1) { articlesDao.findArticleById(articleId) }
  }

  @Test
  fun getArticleById_whenArticleHasNullFields_shouldReturnArticleWithNullFields() = runTest {
    // Given
    val articleId = "123"
    val entity = ArticlesEntity(
      id = articleId,
      title = null,
      description = null,
      urlToImage = null,
      page = 1
    )

    coEvery { articlesDao.findArticleById(articleId) } returns entity

    // When
    val result = repository.getArticleById(articleId)

    // Then
    assertNotNull(result)
    assertEquals(articleId, result.id)
    assertNull(result.title)
    assertNull(result.description)
    assertNull(result.imageUrl)
  }

  @Test
  fun getTopHeadlines_whenMultiplePages_shouldCacheWithCorrectPageNumbers() = runTest {
    // Given
    val page1 = 1
    val page2 = 2
    val pageSize = 10

    val articleModel1 = ArticleModel(
      title = "Page 1 Article",
      description = "Description 1",
      urlToImage = "https://example.com/image1.jpg"
    )
    val articleModel2 = ArticleModel(
      title = "Page 2 Article",
      description = "Description 2",
      urlToImage = "https://example.com/image2.jpg"
    )

    coEvery { service.getTopHeadlines(page = page1, pageSize = pageSize) } returns Result.success(
      ArticlesResponseModel(articles = listOf(articleModel1))
    )
    coEvery { service.getTopHeadlines(page = page2, pageSize = pageSize) } returns Result.success(
      ArticlesResponseModel(articles = listOf(articleModel2))
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    repository.getTopHeadlines(page1, pageSize)
    repository.getTopHeadlines(page2, pageSize)

    // Then
    coVerify(exactly = 2) { articlesDao.insertOrUpdateArticles(any()) }
  }

  @Test
  fun getTopHeadlines_whenServiceReturnsSuccess_shouldNotQueryCache() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val responseModel = ArticlesResponseModel(articles = listOf())

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    repository.getTopHeadlines(page, pageSize)

    // Then
    coVerify(exactly = 0) { articlesDao.getArticlesForPage(any()) }
  }

  @Test
  fun getTopHeadlines_whenServiceFails_shouldNotInsertIntoCache() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val exception = Exception("Network error")
    val cachedEntity = ArticlesEntity(
      id = "1",
      title = "Cached Article",
      description = "Cached Description",
      urlToImage = "https://example.com/cached.jpg",
      page = page
    )

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.failure(
      exception
    )
    coEvery { articlesDao.getArticlesForPage(page) } returns listOf(cachedEntity)

    // When
    repository.getTopHeadlines(page, pageSize)

    // Then
    coVerify(exactly = 0) { articlesDao.insertOrUpdateArticles(any()) }
  }

  @Test
  fun getArticleById_whenArticleNotFound_shouldThrowException() = runTest {
    // Given
    val articleId = "non-existent-id"
    val exception = Exception("Article not found")

    coEvery { articlesDao.findArticleById(articleId) } throws exception

    // When & Then
    try {
      repository.getArticleById(articleId)
      fail("Expected exception to be thrown")
    } catch (e: Exception) {
      assertEquals("Article not found", e.message)
    }
  }

  @Test
  fun getTopHeadlines_whenServiceReturnsSuccess_shouldMapAllFieldsCorrectly() = runTest {
    // Given
    val page = 1
    val pageSize = 10
    val articleModel = ArticleModel(
      title = "Complete Article",
      description = "Complete Description",
      urlToImage = "https://example.com/complete.jpg"
    )
    val responseModel = ArticlesResponseModel(articles = listOf(articleModel))

    coEvery { service.getTopHeadlines(page = page, pageSize = pageSize) } returns Result.success(
      responseModel
    )
    coEvery { articlesDao.insertOrUpdateArticles(any()) } returns Unit

    // When
    val result = repository.getTopHeadlines(page, pageSize)

    // Then
    assertTrue(result.isSuccess)
    val articles = result.getOrNull()
    assertNotNull(articles)
    assertEquals(1, articles?.size)

    val article = articles?.first()
    assertNotNull(article)
    assertEquals("Complete Article", article?.title)
    assertEquals("Complete Description", article?.description)
    assertEquals("https://example.com/complete.jpg", article?.imageUrl)
    assertNotNull(article?.id)
  }
}

