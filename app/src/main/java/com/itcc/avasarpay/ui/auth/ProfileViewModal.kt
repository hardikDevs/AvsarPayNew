package com.itcc.avasarpay.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.repository.ProfileRepository
import com.itcc.avasarpay.utils.DispatcherProvider
import com.itcc.stonna.utils.createPartFromString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 *Created By Hardik on 19-03-2024.
 */
@HiltViewModel
class ProfileViewModal @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginModal>>(UiState.Idle)

    val uiState: StateFlow<UiState<LoginModal>> = _uiState


    fun updateProfile(
        id: Int,
        name: String,
        phone: String,
        file: String,
        fileName:String

    ) {

        val map: MutableMap<String, RequestBody> = mutableMapOf()
        map["name"] = createPartFromString(name)
        map["phone"] = createPartFromString(phone)


        val requestFile = File(file)
            .asRequestBody("image/*".toMediaType())

        val multipartImage = MultipartBody.Part.createFormData(
            "profile_image", fileName,
            requestFile
        );
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            profileRepository.updateProfile(id, map, multipartImage)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }


}