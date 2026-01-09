package com.innovation.readify.designsystem.componentts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.innovation.readify.designsystem.theme.LocalSpacing
import com.innovation.readify.designsystem.theme.ReadifyTheme

@Composable
fun ErrorFullScreen(
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  val spacing = LocalSpacing.current

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxSize()
    ) {
      Text(
        text = "Something went wrong",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = spacing.l)
      )

      Button(
        onClick = onRetry
      ) {
        Text(text = "Retry")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ErrorFullScreenPreview() {
  ReadifyTheme {
    ErrorFullScreen(
      onRetry = {}
    )
  }
}
