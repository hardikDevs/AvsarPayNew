package com.itcc.avasarpay.data.api


import com.itcc.avasarpay.data.modal.CateListRes
import com.itcc.avasarpay.data.modal.GuestModal
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.MagicLinkModal
import com.itcc.avasarpay.data.modal.RegisterReq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST("login")
    suspend fun login(@Body body: RequestBody): Response<LoginModal>

    @POST("magic-link")
    suspend fun sendMagicLink(@Body body: RequestBody): Response<MagicLinkModal>

    @POST("signup")
    suspend fun register(@Body registerReq: RegisterReq): Response<LoginModal>

    @GET("categories")
    suspend fun getCategoryList(): Response<CateListRes>

    @GET("contacts")
    suspend fun getGuestList(): Response<GuestModal>

    @Multipart
    @JvmSuppressWildcards
    @POST("user/update/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int, @PartMap params: Map<String, RequestBody>,
        @Part file :MultipartBody.Part
    ): Response<LoginModal>
}