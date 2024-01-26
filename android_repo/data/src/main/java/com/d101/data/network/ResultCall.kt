package com.d101.data.network

import com.d101.data.model.ApiResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResultCall<T>(
    private val callDelegate: Call<T>,
) : Call<ApiResult<T>> {

    override fun clone(): Call<ApiResult<T>> = ResultCall(callDelegate.clone())

    override fun execute(): Response<ApiResult<T>> =
        throw UnsupportedOperationException("ResponseCall does not support execute.")

    override fun isExecuted() = callDelegate.isExecuted
    override fun cancel() = callDelegate.cancel()
    override fun isCanceled() = callDelegate.isCanceled
    override fun request(): Request = callDelegate.request()
    override fun timeout(): Timeout = callDelegate.timeout()
    override fun enqueue(callback: Callback<ApiResult<T>>) =
        callDelegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(ApiResult.Success(body)),
                        )
                    } else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                ApiResult.Failure.UnexpectedError(
                                    NullPointerException(
                                        "body is null",
                                    ),
                                ),
                            ),
                        )
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: ""

                    callback.onResponse(
                        this@ResultCall,
                        Response.success(
                            ApiResult.Failure.HttpError(response.code(), errorMessage),
                        ),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result: ApiResult<T> = when (t) {
                    is IOException -> ApiResult.Failure.NetworkError(t)
                    else -> ApiResult.Failure.UnexpectedError(t)
                }
                callback.onResponse(this@ResultCall, Response.success(result))
            }
        })
}
