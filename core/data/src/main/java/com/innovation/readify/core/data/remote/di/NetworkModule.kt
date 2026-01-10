package com.innovation.readify.core.data.remote.di

import com.innovation.core.data.BuildConfig
import com.innovation.readify.core.data.remote.adaptorfactory.ResultCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
      level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
      else HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .build()
  }

  @Singleton
  @Provides
  fun provideJsonConverterFactory(json: Json): Converter.Factory {
    val mediaType = "application/json".toMediaType()
    return json.asConverterFactory(mediaType)
  }

  @Provides
  fun provideJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = true
    encodeDefaults = true
    prettyPrint = true

  }

  @Singleton
  @Provides
  fun provideCallAdapterFactory(): CallAdapter.Factory = ResultCallAdapterFactory()

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    json: Json,
    converterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory
  ): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .addConverterFactory(converterFactory)
      .addCallAdapterFactory(callAdapterFactory)
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory(contentType))
      .build()
  }
}