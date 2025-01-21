package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.AllGuestModal
import com.itcc.avasarpay.data.modal.GuestModal
import com.itcc.avasarpay.data.modal.LoginModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {

    fun getEventGuestList(@Body body: RequestBody ): Flow<GuestModal> {
        return flow {
            emit(apiRequest { networkService.getEventGuestList(body) })
        }
    }

    fun getAllGuestList(): Flow<AllGuestModal> {
        return flow {
            emit(apiRequest { networkService.getGuestList() })
        }
    }
 fun createContacts(@Body body: RequestBody): Flow<LoginModal> {
        return flow {
            emit(apiRequest { networkService.createContacts(body) })
        }
    }


}
