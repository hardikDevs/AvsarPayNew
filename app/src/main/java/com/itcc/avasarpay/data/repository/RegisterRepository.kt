package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.RegisterReq
import com.itcc.avasarpay.data.modal.RegisterRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {

    suspend fun register(@Body registerReq: RegisterReq): Flow<RegisterRes> {
        return flow {
            emit(apiRequest { networkService.register(registerReq) })
        }
    }


}
