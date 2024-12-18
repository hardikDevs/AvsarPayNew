package com.itcc.avasarpay.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.repository.LoginRepository
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
class LoginViewModal @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginModal>>(UiState.Idle)

    val uiState: StateFlow<UiState<LoginModal>> = _uiState


    fun login(
       token:String
    ) {
        val jsonBody = JSONObject()
        try {

            jsonBody.put("token", token)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RetrofitRequestBody.wrapParams(jsonBody.toString())

        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            loginRepository.login(body)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }





}