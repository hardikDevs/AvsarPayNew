package com.itcc.avasarpay.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.modal.RegisterReq
import com.itcc.avasarpay.data.modal.RegisterRes
import com.itcc.avasarpay.data.repository.LoginRepository
import com.itcc.avasarpay.data.repository.RegisterRepository
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
class RegisterViewModal @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<RegisterRes>>(UiState.Idle)

    val uiState: StateFlow<UiState<RegisterRes>> = _uiState


    fun register(
        body: RegisterReq,

    ) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            registerRepository.register(body)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }



}