package com.innovation.readify.features.articles.domain.usecase

import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
  private val articlesRepository: ArticlesRepository
) {

  suspend operator fun invoke(
    page: Int,
    pageSize: Int,
  ) = articlesRepository.getTopHeadlines(page, pageSize)

}