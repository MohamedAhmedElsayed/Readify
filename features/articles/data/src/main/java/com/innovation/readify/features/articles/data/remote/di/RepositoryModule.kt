package com.innovation.readify.features.articles.data.remote.di

import com.innovation.readify.features.articles.data.repository.ArticlesRepositoryImp
import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

  @Binds
  fun bindArticlesRepository(articlesRepository: ArticlesRepositoryImp): ArticlesRepository

}
