package com.d101.data.network

import com.d101.data.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapter<R : Any>(private val responseType: Type) :
    CallAdapter<R, Call<ApiResult<R>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<ApiResult<R>> = ResultCall(call)

    class Factory : CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit,
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != Call::class.java) {
                return null
            }

            check(returnType is ParameterizedType) {
                "return type must be parameterized as Call<ApiResult<F>> or Call<ApiResult<out F>>"
            }

            val responseType = getParameterUpperBound(0, returnType)
            if (getRawType(responseType) != ApiResult::class.java) {
                return null
            }
            check(responseType is ParameterizedType) {
                "Response must be parameterized as ApiResult<Foo> or ApiResult<out Foo>"
            }

            val successBodyType = getParameterUpperBound(0, responseType)

            return ResultCallAdapter<Any>(successBodyType)
        }
    }
}
