package com.innovation.readify.features.articles.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.innovation.readify.features.articles.presentation.articledetails.ui.ArticleDetailsScreen
import com.innovation.readify.features.articles.presentation.articleslist.ui.ArticlesListScreen
import com.innovation.readify.presentation.navigation.LocalNavController

fun NavGraphBuilder.articlesNavGraph() {
  composable<ArticlesRoute.ArticlesList> {
    val navController = LocalNavController.current
    ArticlesListScreen { navController.navigate(ArticlesRoute.ArticleDetails(it)) }

  }
  composable<ArticlesRoute.ArticleDetails> {
    val navController = LocalNavController.current
    ArticleDetailsScreen(onBackClick = { navController.popBackStack() })

  }
}