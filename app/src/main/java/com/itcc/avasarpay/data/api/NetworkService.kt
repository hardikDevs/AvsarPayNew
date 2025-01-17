package com.itcc.avasarpay.data.api


import com.itcc.avasarpay.data.modal.CategoryListModal
import com.itcc.avasarpay.data.modal.GuestModal
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.MagicLinkModal
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

    @GET("categories")
    suspend fun getCategoryList(): Response<CategoryListModal>

    @GET("contacts")
    suspend fun getGuestList(): Response<GuestModal>

    @Multipart
    @JvmSuppressWildcards
    @POST("user/update/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int, @PartMap params: Map<String, RequestBody>,
        @Part file :MultipartBody.Part
    ): Response<LoginModal>

    @Multipart
    @JvmSuppressWildcards
    @POST("event")
    suspend fun createEvent(
       @PartMap params: Map<String, RequestBody>,
        @Part groomPhoto :MultipartBody.Part?,
       @Part bridePhoto :MultipartBody.Part?,
       @Part couplePhoto1 :MultipartBody.Part?,
       @Part couplePhoto2 :MultipartBody.Part?,
       @Part couplePhoto3 :MultipartBody.Part?,
       @Part posterPhoto :MultipartBody.Part?,
       @Part invitationCardPhoto : MultipartBody.Part?,
       @Part husbandPhoto : MultipartBody.Part?,
       @Part wifePhoto : MultipartBody.Part?
    ): Response<LoginModal>
}