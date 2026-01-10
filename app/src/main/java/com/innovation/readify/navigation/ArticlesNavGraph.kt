package com.innovation.readify.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.innovation.readify.features.articles.presentation.navigation.ArticlesRoute
import com.innovation.readify.features.articles.presentation.navigation.articlesNavGraph
import com.innovation.readify.presentation.navigation.LocalNavController

@Composable
fun ArticlesNavHost() {
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
