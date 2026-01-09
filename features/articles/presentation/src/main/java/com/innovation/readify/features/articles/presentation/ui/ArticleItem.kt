package com.innovation.readify.features.articles.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.innovation.readify.designsystem.componentts.ImageLoadingError
import com.innovation.readify.designsystem.componentts.LoadingItem
import com.innovation.readify.designsystem.theme.LocalSpacing
import com.innovation.readify.designsystem.theme.ReadifyTheme
import com.innovation.readify.features.articles.domain.model.Article

@Composable
fun ArticleItem(article: Article, onArticleClick: (String) -> Unit) {
  val spacing = LocalSpacing.current
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .clickable {
//        onArticleClick(article.id)
      },
    shape = RoundedCornerShape(spacing.s),
    elevation = CardDefaults.cardElevation(spacing.xxs)
  ) {
    Box {
      SubcomposeAsyncImage(
        model = article.imageUrl ?: "",
        contentDescription = article.title,
        modifier = Modifier
          .fillMaxSize(),
        contentScale = ContentScale.Crop,
        loading = { LoadingItem() },
        error = { ImageLoadingError() }
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.75f)
              ),
              startY = 300f
            )
          )
      )
      article.title?.let { title ->
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(spacing.m),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ArticleItemPreview() {
  ReadifyTheme {
    ArticleItem(
      article = Article(
        title = "Sample Article Title That Might Be Long and Wrap to Multiple Lines",
        imageUrl = "https://picsum.photos/400/200",
        description = "This is a sample article description for preview purposes."
      ),
      onArticleClick = {}
    )
  }
}

@Preview(showBackground = true, name = "Article Item - Short Title")
@Composable
fun ArticleItemShortTitlePreview() {
  ReadifyTheme {
    ArticleItem(
      article = Article(
        title = "Short Title",
        imageUrl = "https://picsum.photos/400/200",
        description = "A shorter title example."
      ),
      onArticleClick = {}
    )
  }
}

@Preview(showBackground = true, name = "Article Item - No Image")
@Composable
fun ArticleItemNoImagePreview() {
  ReadifyTheme {
    ArticleItem(
      article = Article(
        title = "Article Without Image",
        imageUrl = null,
        description = "This article has no image URL."
      ),
      onArticleClick = {}
    )
  }
}