package com.innovation.readify.features.articles.data.remote.remotedatasource

import com.innovation.core.data.BuildConfig
import com.innovation.readify.features.articles.data.model.ArticlesModel
import retrofit2.http.GET
import retrofit2.http.Query

private const val Query = "sport"

interface ArticlesService {

  @GET("top-headlines")
  suspend fun getTopHeadlines(
    @Query("q") category: String = Query,
    @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    @Query("page") page: Int,
    @Query("pageSize") pageSize: Int,
  ): Result<ArticlesModel>
}
