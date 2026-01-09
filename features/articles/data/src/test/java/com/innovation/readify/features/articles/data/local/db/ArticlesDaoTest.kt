package com.innovation.readify.features.articles.data.local.db

import androidx.test.core.app.ApplicationProvider
import com.innovation.readify.features.articles.data.MainDispatcherRule
import com.innovation.readify.features.articles.data.local.entities.ArticlesEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import androidx.room.Room

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ArticlesDaoTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var database: ArticlesDataBase
    private lateinit var dao: ArticlesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArticlesDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = database.articlesDeo()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetArticleById_shouldReturnInsertedArticle() = runTest {
        val article = ArticlesEntity(
            id = "1",
            title = "Test Article",
            description = "Test Description",
            urlToImage = "https://example.com/image.jpg",
            page = 1
        )
        dao.insertOrUpdateArticles(listOf(article))

        val loadedArticle = dao.findArticleById("1")

        assertEquals(article.id, loadedArticle.id)
        assertEquals(article.title, loadedArticle.title)
        assertEquals(article.description, loadedArticle.description)
        assertEquals(article.urlToImage, loadedArticle.urlToImage)
        assertEquals(article.page, loadedArticle.page)
    }

    @Test
    fun insertAndGetArticlesForPage_shouldReturnAllArticlesForPage() = runTest {
        val article1 = ArticlesEntity(
            id = "1",
            title = "First Article",
            description = "First Description",
            urlToImage = "https://example.com/image1.jpg",
            page = 1
        )
        val article2 = ArticlesEntity(
            id = "2",
            title = "Second Article",
            description = "Second Description",
            urlToImage = "https://example.com/image2.jpg",
            page = 1
        )
        val article3 = ArticlesEntity(
            id = "3",
            title = "Third Article",
            description = "Third Description",
            urlToImage = "https://example.com/image3.jpg",
            page = 2
        )

        dao.insertOrUpdateArticles(listOf(article1, article2, article3))

        val page1Articles = dao.getArticlesForPage(1)
        val page2Articles = dao.getArticlesForPage(2)

        assertEquals(2, page1Articles.size)
        assertTrue(page1Articles.any { it.id == "1" })
        assertTrue(page1Articles.any { it.id == "2" })

        assertEquals(1, page2Articles.size)
        assertTrue(page2Articles.any { it.id == "3" })
    }

    @Test
    fun insertOrUpdateArticles_shouldReplaceExistingArticle() = runTest {
        val article1 = ArticlesEntity(
            id = "1",
            title = "Original Title",
            description = "Original Description",
            urlToImage = "https://example.com/image1.jpg",
            page = 1
        )
        dao.insertOrUpdateArticles(listOf(article1))

        val article2 = ArticlesEntity(
            id = "1",
            title = "Updated Title",
            description = "Updated Description",
            urlToImage = "https://example.com/image2.jpg",
            page = 2
        )
        dao.insertOrUpdateArticles(listOf(article2))

        val loadedArticle = dao.findArticleById("1")

        assertEquals("1", loadedArticle.id)
        assertEquals("Updated Title", loadedArticle.title)
        assertEquals("Updated Description", loadedArticle.description)
        assertEquals("https://example.com/image2.jpg", loadedArticle.urlToImage)
        assertEquals(2, loadedArticle.page)
    }
}

