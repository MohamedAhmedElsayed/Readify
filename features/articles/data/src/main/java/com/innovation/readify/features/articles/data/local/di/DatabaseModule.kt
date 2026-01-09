package com.innovation.readify.features.articles.data.local.di


import android.app.Application
import androidx.room.Room
import com.innovation.readify.features.articles.data.local.db.ArticlesDao
import com.innovation.readify.features.articles.data.local.db.ArticlesDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideDatabase(app: Application) = Room.databaseBuilder(
    app,
    klass = ArticlesDataBase::class.java,
    name = "readify_articles_db"
  ).fallbackToDestructiveMigration()
    .build()


  @Provides
  fun provideArticlesDao(db: ArticlesDataBase): ArticlesDao {
    return db.articlesDeo()
  }
}
