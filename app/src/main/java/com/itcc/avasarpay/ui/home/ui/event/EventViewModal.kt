package com.itcc.avasarpay.ui.home.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.api.RetrofitRequestBody
import com.itcc.avasarpay.data.modal.LoginModal
import com.itcc.avasarpay.data.repository.CategoryRepository
import com.itcc.avasarpay.data.repository.EventRepository
import com.itcc.avasarpay.data.repository.LoginRepository
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
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

/**
 *Created By Hardik on 19-03-2024.
 */
@HiltViewModel
class EventViewModal @Inject constructor(
    private val eventRepository: EventRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<LoginModal>>(UiState.Idle)

    val uiState: StateFlow<UiState<LoginModal>> = _uiState

    fun createEvent(
        title: String,
        category_id: String,
        venue: String,
        templete_id: String,
        welcome_description: String,


         // cateId==1
        event_date: String,
        groom_name: String,
        bride_name: String,
        groom_photo: String,
        bride_photo: String,
        poster_photo: String,

        // cateId==2
        birthday_celebrant: String,
        how_old_celebrant: String,

        // cateId==3
        husband_name: String,
        wife_name: String,
        how_long_married: String,
        couple_photo1: String,
        couple_photo2: String,
        couple_photo3: String,
        invitation_card_photo: String,
        husband_photo: String,
        wife_photo: String,

        // cateId==4
        father_name: String,
        mother_name: String,
        expected_date: String,
        gender: String,
        baby_name: String,



    ) {

        val map: MutableMap<String, RequestBody> = mutableMapOf()
        map["title"] = createPartFromString(title)
        map["category_id"] = createPartFromString(category_id)
        map["event_date"] = createPartFromString(event_date)
        map["venue"] = createPartFromString(venue)
        map["template_id"] = createPartFromString(templete_id)
        map["welcome_description"] = createPartFromString(welcome_description)

        map["groom_name"] = createPartFromString(groom_name)
        map["bride_name"] = createPartFromString(bride_name)

        map["birthday_celebrant"] = createPartFromString(birthday_celebrant)
        map["how_old_is_celebrant"] = createPartFromString(how_old_celebrant)

        map["husband_name"] = createPartFromString(husband_name)
        map["wife_name"] = createPartFromString(wife_name)
        map["how_long_have_you_been_married"] = createPartFromString(how_long_married)

        map["father_name"] = createPartFromString(father_name)
        map["mother_name"] = createPartFromString(mother_name)
        map["expected_date"] = createPartFromString(expected_date)
        map["do_you_know_gender"] = createPartFromString(gender)
        map["have_you_decided_baby_name"] = createPartFromString(baby_name)

        var  groomPhoto : MultipartBody.Part? = null
        var  bridePhoto : MultipartBody.Part? = null
        var  couplePhoto1 : MultipartBody.Part? = null
        var  couplePhoto2 : MultipartBody.Part? = null
        var  couplePhoto3 : MultipartBody.Part? = null
        var  posterPhoto : MultipartBody.Part? = null
        var  invitationCardPhoto : MultipartBody.Part? = null
        var husbandPhoto : MultipartBody.Part? = null
        var wifePhoto : MultipartBody.Part? = null

        if (groom_photo.isNotEmpty()){
             groomPhoto = MultipartBody.Part.createFormData(
                "groom_photo", "image",
                File(groom_photo)
                    .asRequestBody("image/*".toMediaType())
            );
        }


         if (bride_photo.isNotEmpty()){
             bridePhoto = MultipartBody.Part.createFormData(
                 "bride_photo", "image",
                 File(bride_photo)
                     .asRequestBody("image/*".toMediaType())
             );
         }

        if (couple_photo1.isNotEmpty()){
             couplePhoto1 = MultipartBody.Part.createFormData(
                "couple_photo1", "image",
                File(couple_photo1)
                    .asRequestBody("image/*".toMediaType())
            );
        }


        if (couple_photo2.isNotEmpty()){
            couplePhoto2 = MultipartBody.Part.createFormData(
                "couple_photo2", "image",
                File(couple_photo2)
                    .asRequestBody("image/*".toMediaType())
            );
        }


        if (couple_photo3.isNotEmpty()){
             couplePhoto3 = MultipartBody.Part.createFormData(
                "couple_photo3", "image",
                File(couple_photo3)
                    .asRequestBody("image/*".toMediaType())
            );
        }

        if (poster_photo.isNotEmpty()){
             posterPhoto = MultipartBody.Part.createFormData(
                "poster_image", "image",
                File(poster_photo)
                    .asRequestBody("image/*".toMediaType()))
        }

          if (invitation_card_photo.isNotEmpty()){
               invitationCardPhoto = MultipartBody.Part.createFormData(
                  "invitation_card", "image",
                  File(invitation_card_photo)
                      .asRequestBody("image/*".toMediaType())
              );
          }

        if (husband_photo.isNotEmpty()){
             husbandPhoto = MultipartBody.Part.createFormData(
                "husband_photo", "image",
                File(husband_photo)
                    .asRequestBody("image/*".toMediaType())
            );
        }

        if (wife_photo.isNotEmpty()){
             wifePhoto = MultipartBody.Part.createFormData(
                "wife_photo", "image",
                File(wife_photo)
                    .asRequestBody("image/*".toMediaType())
            );
        }



        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            eventRepository.createEvent(map, groomPhoto, bridePhoto, couplePhoto1, couplePhoto2, couplePhoto3, posterPhoto, invitationCardPhoto, husbandPhoto, wifePhoto)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }


}