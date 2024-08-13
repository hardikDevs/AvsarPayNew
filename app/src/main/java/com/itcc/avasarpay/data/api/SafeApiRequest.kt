package com.itcc.avasarpay.data.api


import com.itcc.avasarpay.utils.Util
import com.itcc.stonna.utils.ApiException
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        when (response.code()) {
            200, 201 -> return response.body()!!
            500 -> throw ApiException("Request failed with error code: 500 Internal Server Error")
            404 -> throw ApiException("Request failed with error code: 404 " + response.message())
            502 -> throw ApiException("Request failed with error code: 502 Bad Gateway")
            408 -> throw Exception("Request Timeout")
            else ->
                throw ApiException(
                    Util.jsonData(response.errorBody()!!.string())
                )
        }
    }
}