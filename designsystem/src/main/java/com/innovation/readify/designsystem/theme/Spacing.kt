package com.innovation.readify.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
  val xxs: Dp = 4.dp,
  val xs: Dp = 8.dp,
  val s: Dp = 12.dp,
  val m: Dp = 16.dp,
  val l: Dp = 24.dp,
  val xl: Dp = 32.dp,
  val xxl: Dp = 40.dp,
  val xxxl: Dp = 48.dp,
  val xxxxl: Dp = 64.dp,
  val xxxxxl: Dp = 80.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
