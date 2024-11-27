package com.itcc.avasarpay.ui.home.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.repository.CategoryRepository
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
class EventViewModal @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginModal>>(UiState.Idle)

    val uiState: StateFlow<UiState<LoginModal>> = _uiState



}