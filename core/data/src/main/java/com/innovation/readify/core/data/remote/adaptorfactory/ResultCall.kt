package com.innovation.readify.core.data.remote.adaptorfactory


import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

internal class ResultCall<T : Any>(
  private val delegate: Call<T>,
  private val responseType: Type,
) : Call<Result<T>> {

  override fun enqueue(callback: Callback<Result<T>>) {
    delegate.enqueue(object : Callback<T> {

      override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = response.toResult()
        callback.onResponse(
          this@ResultCall,
          Response.success(result)
        )
      }

      override fun onFailure(call: Call<T>, t: Throwable) {

        callback.onResponse(
          this@ResultCall,
          Response.success(Result.failure(t))
        )
      }

      private fun Response<T>.toResult() =
        if (isSuccessful) asSuccess() else asFailure()

      @Suppress("UNCHECKED_CAST")
      private fun Response<T>.asSuccess() = when (responseType) {
        Unit::class.java -> Result.success(Unit as T)
        else -> Result.success(body()!!)
      }

      private fun Response<T>.asFailure(): Result<T> {
        return Result.failure(Exception("General exception with code ${code()}"))
      }
    })
  }

  override fun clone(): ResultCall<T> = ResultCall(
    delegate = delegate.clone(),
    responseType = responseType,
  )

  override fun request(): Request = delegate.request()
  override fun cancel(): Unit = delegate.cancel()
  override fun isCanceled(): Boolean = delegate.isCanceled
  override fun isExecuted(): Boolean = delegate.isExecuted
  override fun execute(): Response<Result<T>> = throw UnsupportedOperationException()
  override fun timeout(): Timeout = delegate.timeout()
}


