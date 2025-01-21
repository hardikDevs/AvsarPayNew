package com.itcc.avasarpay.ui.home.ui.event.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.LocalContactModal
import com.itcc.avasarpay.data.repository.ContactRepository
import com.itcc.avasarpay.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created By Sunny on 13-12-2024.
 */
@HiltViewModel
class ContactViewModel @Inject constructor( private val contactRepository: ContactRepository,private val dispatcherProvider: DispatcherProvider) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LocalContactModal>>(UiState.Loading)
    val uiState: StateFlow<UiState<LocalContactModal>> = _uiState

    fun loadContacts() {
        contactRepository.clearContacts()
        viewModelScope.launch(dispatcherProvider.main) {
            contactRepository.fetchContacts()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }


}