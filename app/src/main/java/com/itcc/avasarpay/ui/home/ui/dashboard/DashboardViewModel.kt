package com.itcc.avasarpay.ui.home.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.BaseModal
import com.itcc.avasarpay.data.modal.CateListRes
import com.itcc.avasarpay.data.repository.CategoryRepository
import com.itcc.avasarpay.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CateListRes>>(UiState.Idle)
    val uiState: StateFlow<UiState<CateListRes>> = _uiState


    fun getCategoryList() {
        viewModelScope.launch(dispatcherProvider.main) {
            categoryRepository.getCategoryList()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}