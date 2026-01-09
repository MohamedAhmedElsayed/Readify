package com.innovation.readify.features.articles.data.di

import com.innovation.readify.features.articles.data.remote.remotedatasource.ArticlesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ServiceModule {

  @Provides
  fun provideArticlesService(retrofit: Retrofit): ArticlesService {
    return retrofit.create(ArticlesService::class.java)
  }
}