package com.itcc.avasarpay.data.api



import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object RetrofitRequestBody {

    fun wrapParams(params: String): RequestBody {
        return params
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }



}