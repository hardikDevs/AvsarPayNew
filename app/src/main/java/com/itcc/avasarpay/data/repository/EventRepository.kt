package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.LoginModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {


    fun createEvent(
        params: Map<String, RequestBody>,
        groomPhoto: MultipartBody.Part?,
        bridePhoto: MultipartBody.Part?,
        couplePhoto1: MultipartBody.Part?,
        couplePhoto2: MultipartBody.Part?,
        couplePhoto3: MultipartBody.Part?,
        posterPhoto: MultipartBody.Part?,
        invitationCardPhoto: MultipartBody.Part?,
        husbandPhoto: MultipartBody.Part?,
        wifePhoto: MultipartBody.Part?
    ): Flow<LoginModal> {
        return flow {
            emit(apiRequest { networkService.createEvent(params, groomPhoto, bridePhoto, couplePhoto1, couplePhoto2, couplePhoto3, posterPhoto, invitationCardPhoto, husbandPhoto, wifePhoto) })
        }
    }

}
