package com.innovation.readify.features.articles.presentation.articleslist.model

import com.innovation.readify.features.articles.domain.model.Article

data class ArticleUiModel(
  val id: String,
  val title: String,
  val description: String,
  val imageUrl: String
)

fun Article.toUiModel() = ArticleUiModel(
  id = this.id.orEmpty(),
  title = this.title.orEmpty(),
  description = this.description.orEmpty(),
  imageUrl = this.imageUrl.orEmpty(),
)


fun List<Article>?.toArticlesUiModelList() = this?.map { it.toUiModel() } ?: emptyList()
