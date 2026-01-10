package com.innovation.readify.features.articles.presentation.articleslist.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.innovation.readify.designsystem.theme.LocalSpacing

@Composable
fun EmptyScreenWithRetry(
  onRetryClick: () -> Unit
) {
  val spacing = LocalSpacing.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(vertical = spacing.xxxxxl, horizontal = spacing.xxl),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.Refresh,
      contentDescription = "error",
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(spacing.xxxxl)
    )

    Spacer(modifier = Modifier.height(spacing.m))

    Text(
      text = "No data available, Please  try to load again",
      style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(spacing.m))

    Button(onClick = onRetryClick) {
      Text("Retry")
    }
  }
}
