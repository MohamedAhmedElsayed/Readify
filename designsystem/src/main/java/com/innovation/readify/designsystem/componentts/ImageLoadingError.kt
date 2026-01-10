package com.innovation.readify.designsystem.componentts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.innovation.readify.designsystem.theme.LocalSpacing

@Composable
fun ImageLoadingError() {
  val spacing = LocalSpacing.current
  Box(
    modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(spacing.xs)),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.Warning,
      contentDescription = "error loading image",
      modifier = Modifier.size(spacing.xxxl),
    )
  }
}
