package com.innovation.readify.core.data.remote.adaptorfactory


import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

@Suppress("UnusedPrivateMember")
class ResultCallAdapterFactory @Inject constructor(
) : CallAdapter.Factory() {

  override fun get(
    returnType: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): CallAdapter<*, *>? {
    if (getRawType(returnType) != Call::class.java) return null
    val callType = getParameterUpperBound(0, returnType as ParameterizedType)
    return if (getRawType(callType) == Result::class.java) {
      ResultCallAdapter(
        responseType = getParameterUpperBound(0, callType as ParameterizedType),
      )
    } else {
      null
    }
  }

  private class ResultCallAdapter(
    private val responseType: Type,
  ) : CallAdapter<Type, Call<Result<Type>>> {

    override fun responseType(): Type = responseType
    override fun adapt(call: Call<Type>): Call<Result<Type>> = ResultCall(
      delegate = call,
      responseType = responseType,
    )
  }
}
