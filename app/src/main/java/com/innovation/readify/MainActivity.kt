package com.innovation.readify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.innovation.readify.designsystem.theme.ReadifyTheme
import com.innovation.readify.features.articles.presentation.navigation.ArticlesRoute
import com.innovation.readify.features.articles.presentation.navigation.articlesNavGraph
import com.innovation.readify.presentation.navigation.LocalNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ReadifyTheme {
        PostsNavGraph()
      }
    }
  }
}

@Composable
private fun PostsNavGraph() {
  val navController = rememberNavController()
  CompositionLocalProvider(LocalNavController provides navController) {
    NavHost(
      navController = navController,
      startDestination = ArticlesRoute.ArticlesList
    ) {
      articlesNavGraph()
    }
  }
}


