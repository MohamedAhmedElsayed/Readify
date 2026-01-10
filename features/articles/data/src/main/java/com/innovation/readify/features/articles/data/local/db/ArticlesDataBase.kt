package com.innovation.readify.features.articles.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.innovation.readify.features.articles.data.local.entities.ArticlesEntity

@Database(entities = [ArticlesEntity::class], version = 1, exportSchema = false)
abstract class ArticlesDataBase : RoomDatabase() {
  abstract fun articlesDeo(): ArticlesDao
}