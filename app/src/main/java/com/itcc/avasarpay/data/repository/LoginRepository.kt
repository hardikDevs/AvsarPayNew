package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.LoginModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {

    suspend fun login(@Body body: RequestBody): Flow<LoginModal> {
        return flow {
            emit(apiRequest { networkService.login(body) })
        }
    }


}
