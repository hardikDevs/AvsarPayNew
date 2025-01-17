package com.itcc.avasarpay.ui.home.ui.event.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.data.modal.Contact
import com.itcc.avasarpay.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created By Sunny on 13-12-2024.
 */
@HiltViewModel
class ContactViewModel @Inject constructor( private val contactRepository: ContactRepository) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadContacts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val fetchedContacts = contactRepository.fetchContacts()
                _contacts.value = fetchedContacts
            } catch (e: Exception) {
                _error.value = "Failed to load contacts: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}