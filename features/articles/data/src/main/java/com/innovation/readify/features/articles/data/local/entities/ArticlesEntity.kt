package com.innovation.readify.features.articles.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articles")
data class ArticlesEntity(
  @PrimaryKey
  val id: String,
  val page: Int?,
  val title: String?,
  val description: String?,
  val urlToImage: String?,
)
