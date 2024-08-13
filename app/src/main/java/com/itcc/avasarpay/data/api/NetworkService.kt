package com.itcc.avasarpay.data.api


import com.itcc.avasarpay.data.modal.LoginModal
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST("login")
    suspend fun login(@Body body: RequestBody): Response<LoginModal>


}