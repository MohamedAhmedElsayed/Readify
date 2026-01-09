package com.innovation.readify.features.articles.presentation.articledetails.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.innovation.readify.designsystem.componentts.ImageLoadingError
import com.innovation.readify.designsystem.componentts.LoadingItem
import com.innovation.readify.designsystem.theme.LocalSpacing
import com.innovation.readify.designsystem.theme.ReadifyTheme
import com.innovation.readify.features.articles.presentation.articleslist.model.ArticleUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsContent(
  onBackClick: () -> Unit,
  article: ArticleUiModel?,
  modifier: Modifier = Modifier
) {
  val spacing = LocalSpacing.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Article Details") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(spacing.m)
    ) {
      article?.let {
        if (it.imageUrl.isNotEmpty()) {
          SubcomposeAsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            contentScale = ContentScale.Crop,
            loading = { LoadingItem() },
            error = { ImageLoadingError() }
          )
          Spacer(modifier = Modifier.height(spacing.m))
        }

        Text(
          text = article.title,
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(spacing.xs))

        Text(
          text = article.description,
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}

@Preview(showBackground = true, name = "Article Details - With Article")
@Composable
fun ArticleDetailsContentPreview() {
  ReadifyTheme {
    ArticleDetailsContent(
      onBackClick = {},
      article = ArticleUiModel(
        id = "1",
        title = "Breaking News: Technology Advances in 2024",
        description = "The latest developments in technology and innovation are reshaping our world. From artificial intelligence to quantum computing, we're witnessing unprecedented changes that will transform how we live, work, and interact. This comprehensive article explores the key trends and breakthroughs that are defining the technological landscape in 2024.",
        imageUrl = "https://picsum.photos/800/400?random=1"
      ),
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Article Details - Without Image")
@Composable
fun ArticleDetailsContentNoImagePreview() {
  ReadifyTheme {
    ArticleDetailsContent(
      onBackClick = {},
      article = ArticleUiModel(
        id = "2",
        title = "The Future of Sustainable Energy",
        description = "As the world grapples with climate change, renewable energy sources are becoming increasingly important. Solar and wind power are leading the charge, with new technologies making them more efficient and affordable than ever before. This article examines the latest innovations in clean energy and their potential to transform our energy infrastructure.",
        imageUrl = ""
      ),
      modifier = Modifier
    )
  }
}

@Preview(showBackground = true, name = "Article Details - Loading/Null State")
@Composable
fun ArticleDetailsContentNullPreview() {
  ReadifyTheme {
    ArticleDetailsContent(
      onBackClick = {},
      article = null,
      modifier = Modifier
    )
  }
}
