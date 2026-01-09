package com.innovation.readify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import com.innovation.readify.designsystem.theme.ReadifyTheme
import com.innovation.readify.features.articles.presentation.articleslist.ui.ArticlesListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ReadifyTheme {
        Scaffold(
          topBar = {
            TopAppBar(
              title = { Text(text = "Readify Articles") },
            )
          },
        ) { paddingValues ->
          ArticlesListScreen(modifier = Modifier.padding(paddingValues), onArticleClicked = {})

        }
      }
    }
  }
}



