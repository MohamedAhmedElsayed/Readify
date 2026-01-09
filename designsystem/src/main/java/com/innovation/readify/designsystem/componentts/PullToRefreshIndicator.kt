package com.innovation.readify.designsystem.componentts

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.PositionalThreshold
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.innovation.readify.designsystem.componentts.PullToRefreshIndicatorConstants.CROSSFADE_DURATION_MILLIS
import com.innovation.readify.designsystem.componentts.PullToRefreshIndicatorConstants.SPINNER_SIZE
import com.innovation.readify.designsystem.theme.LocalSpacing
import com.innovation.readify.designsystem.theme.ReadifyTheme

private object PullToRefreshIndicatorConstants {
  const val CROSSFADE_DURATION_MILLIS = 100
  val SPINNER_SIZE = 16.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshIndicator(
  state: PullToRefreshState,
  isRefreshing: Boolean,
  modifier: Modifier = Modifier,
) {
  val spacing = LocalSpacing.current
  Box(
    modifier = modifier.pullToRefreshIndicator(
      state = state,
      isRefreshing = isRefreshing,
      containerColor = PullToRefreshDefaults.containerColor,
      threshold = PositionalThreshold
    ),
    contentAlignment = Alignment.Center
  ) {
    Crossfade(
      targetState = isRefreshing,
      animationSpec = tween(durationMillis = CROSSFADE_DURATION_MILLIS),
      modifier = Modifier.align(Alignment.Center)
    ) { refreshing ->
      if (refreshing) {
        CircularProgressIndicator(Modifier.size(SPINNER_SIZE))
      } else {
        val distanceFraction = { state.distanceFraction.coerceIn(0f, 1f) }
        Icon(
          imageVector = Icons.Filled.Refresh,
          contentDescription = "Refresh",
          modifier = Modifier
            .size(spacing.l)
            .graphicsLayer {
              val progress = distanceFraction()
              this.alpha = progress
              this.scaleX = progress
              this.scaleY = progress
            }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "PullToRefreshIndicator - Refreshing")
@Composable
fun PullToRefreshIndicatorRefreshingPreview() {
  ReadifyTheme {
    val state = rememberPullToRefreshState()
    PullToRefreshIndicator(
      state = state,
      isRefreshing = true,
      modifier = Modifier
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "PullToRefreshIndicator - Not Refreshing")
@Composable
fun PullToRefreshIndicatorNotRefreshingPreview() {
  ReadifyTheme {
    val state = rememberPullToRefreshState()
    PullToRefreshIndicator(
      state = state,
      isRefreshing = false,
      modifier = Modifier
    )
  }
}
