package com.innovation.readify.features.articles.presentation.articleslist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.innovation.readify.designsystem.theme.LocalSpacing


@Composable
fun RetryLoading(onRetry: () -> Unit) {
  val spacing = LocalSpacing.current
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(spacing.m), contentAlignment = Alignment.Center
  ) {
    Button(onClick = onRetry) {
      Text("Retry")
    }
  }
}