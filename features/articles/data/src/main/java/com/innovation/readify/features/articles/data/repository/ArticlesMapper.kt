package com.innovation.readify.features.articles.data.repository

import com.innovation.readify.features.articles.data.model.ArticlesModel
import com.innovation.readify.features.articles.domain.model.Article
import com.innovation.readify.features.articles.domain.model.Articles

fun ArticlesModel.toDomain() = Articles(
  articles = this.articles?.map { Article(it.title, it.urlToImage, it.description) }
)