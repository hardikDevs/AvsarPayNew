package com.itcc.avasarpay.data.api


import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.RegisterReq
import com.itcc.avasarpay.data.modal.RegisterRes
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST("login")
    suspend fun login(@Body body: RequestBody): Response<LoginModal>

    @POST("signup")
    suspend fun register(@Body registerReq: RegisterReq): Response<RegisterRes>


}