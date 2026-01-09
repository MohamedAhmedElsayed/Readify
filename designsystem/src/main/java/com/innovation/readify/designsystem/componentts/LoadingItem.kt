package com.innovation.readify.designsystem.componentts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.innovation.readify.designsystem.theme.LocalSpacing

@Composable
fun LoadingItem() {
  val spacing = LocalSpacing.current
  Box(
    modifier = Modifier
      .fillMaxSize()
      .clip(RoundedCornerShape(spacing.xs)),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator(
      modifier = Modifier.size(spacing.xl)
    )
  }
}
