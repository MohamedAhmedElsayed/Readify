package com.innovation.readify.features.articles.domain.usecase

import com.innovation.readify.features.articles.domain.repository.ArticlesRepository
import javax.inject.Inject

class GetArticleByIdUseCase @Inject constructor(
  private val articlesRepository: ArticlesRepository
) {
  suspend operator fun invoke(articleId: String) =
    articlesRepository.getArticleById(articleId)

}