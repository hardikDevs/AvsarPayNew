package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.MagicLinkModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MagicLinkRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {

    suspend fun sendMagicLink(@Body body: RequestBody): Flow<MagicLinkModal> {
        return flow {
            emit(apiRequest { networkService.sendMagicLink(body) })
        }
    }


}