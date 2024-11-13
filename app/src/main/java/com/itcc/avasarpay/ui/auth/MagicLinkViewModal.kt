package com.itcc.avasarpay.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.MagicLinkModal
import com.itcc.avasarpay.data.repository.LoginRepository
import com.itcc.avasarpay.data.repository.MagicLinkRepository
import com.itcc.avasarpay.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

/**
 *Created By Hardik on 19-03-2024.
 */
@HiltViewModel
class MagicLinkViewModal @Inject constructor(
    private val magicLinkRepository: MagicLinkRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MagicLinkModal>>(UiState.Idle)

    val uiState: StateFlow<UiState<MagicLinkModal>> = _uiState


    fun sendMagicLink(
        emailValue: String,
        fcmToken: String,
        platform:String,
        applicationVersion:String,
        osVersion :String
    ) {
        val jsonBody = JSONObject()
        try {

            jsonBody.put("email", emailValue)
            jsonBody.put("fcm_token", fcmToken)
            jsonBody.put("platform", platform)
            jsonBody.put("application_version", applicationVersion)
            jsonBody.put("os_version", osVersion)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RetrofitRequestBody.wrapParams(jsonBody.toString())

        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            magicLinkRepository.sendMagicLink(body)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}