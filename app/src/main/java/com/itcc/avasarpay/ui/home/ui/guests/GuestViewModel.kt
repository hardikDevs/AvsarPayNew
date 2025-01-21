package com.itcc.avasarpay.ui.home.ui.guests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.AllGuestModal
import com.itcc.avasarpay.data.modal.GuestModal
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.repository.GuestRepository
import com.itcc.avasarpay.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
    private val guestRepository: GuestRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GuestModal>>(UiState.Loading)
    val uiState: StateFlow<UiState<GuestModal>> = _uiState

    private val _uiAllGuestState = MutableStateFlow<UiState<AllGuestModal>>(UiState.Loading)
    val uiAllGuestState: StateFlow<UiState<AllGuestModal>> = _uiAllGuestState

    private val _uiStateContacts = MutableStateFlow<UiState<LoginModal>>(UiState.Idle)
    val uiContacts: StateFlow<UiState<LoginModal>> = _uiStateContacts


    fun getEventGuestList(eventId: String) {
        val jsonBody = JSONObject()
        try {

            jsonBody.put("event_id", eventId)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RetrofitRequestBody.wrapParams(jsonBody.toString())
        viewModelScope.launch(dispatcherProvider.main) {
            guestRepository.getEventGuestList(body)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
    fun getAllGuestList() {
        viewModelScope.launch(dispatcherProvider.main) {
            guestRepository.getAllGuestList()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiAllGuestState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiAllGuestState.value = UiState.Success(it)
                }
        }
    }

    fun createContacts(
        eventId: String,
        array: JSONArray
    ) {
        val jsonBody = JSONObject()
        try {

            jsonBody.put("event_id", eventId)
            jsonBody.put("contacts", array)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RetrofitRequestBody.wrapParams(jsonBody.toString())

        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateContacts.value = UiState.Loading
            guestRepository.createContacts(body)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiStateContacts.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiStateContacts.value = UiState.Success(it)
                }
        }
    }
}