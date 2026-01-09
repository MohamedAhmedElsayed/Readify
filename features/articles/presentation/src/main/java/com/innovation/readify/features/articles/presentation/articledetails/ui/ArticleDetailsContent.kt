package com.innovation.readify.features.articles.presentation.articledetails.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.innovation.readify.designsystem.theme.LocalSpacing
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
          AsyncImage(
            model = it.imageUrl,
            contentDescription = it.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .clip(RoundedCornerShape(spacing.s))
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
