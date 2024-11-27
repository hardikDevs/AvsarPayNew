package com.itcc.avasarpay.data.repository


import com.itcc.avasarpay.data.api.NetworkService
import com.itcc.avasarpay.data.api.SafeApiRequest
import com.itcc.avasarpay.data.modal.GuestModal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestRepository @Inject constructor(private val networkService: NetworkService) :
    SafeApiRequest() {

    suspend fun getGuestList(): Flow<GuestModal> {
        return flow {
            emit(apiRequest { networkService.getGuestList() })
        }
    }


}
