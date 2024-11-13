package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.LoginModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {


    fun updateProfile(
        id: Int,
        params: Map<String, RequestBody>,
        file: MultipartBody.Part
    ): Flow<LoginModal> {
        return flow {
            emit(apiRequest { networkService.updateProfile(id, params, file) })
        }
    }

}
