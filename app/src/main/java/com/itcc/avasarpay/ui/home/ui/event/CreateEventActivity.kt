package com.itcc.avasarpay.ui.home.ui.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.CategoryItem
import com.itcc.avasarpay.data.modal.GuestIteam
import com.itcc.avasarpay.data.modal.TemplatesItem
import com.itcc.avasarpay.databinding.ActivityCreateEventBinding
import com.itcc.avasarpay.databinding.ActivityDashboardBinding
import com.itcc.avasarpay.databinding.ActivityProfileBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.avasarpay.ui.home.ui.guests.GuestAdapter
import com.itcc.avasarpay.ui.home.ui.guests.InviteGuestActivity
import com.itcc.avasarpay.utils.AppConstant
import com.itcc.avasarpay.utils.FileHelper
import com.itcc.avasarpay.utils.Util
import com.itcc.avasarpay.utils.Util.preventMultipleClicks
import com.itcc.avasarpay.utils.Util.setOnClickListener
import com.itcc.stonna.utils.Logger
import com.itcc.stonna.utils.getValue
import com.itcc.stonna.utils.hide
import com.itcc.stonna.utils.hideViews
import com.itcc.stonna.utils.invisible
import com.itcc.stonna.utils.serializable
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateEventActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreateEventBinding

    private val eventViewModal: EventViewModal by viewModels()

    private var categoryItem : CategoryItem?=null

    private var categoryId=0

    private val templetList: MutableList<TemplatesItem> = mutableListOf()

    private lateinit var adapter: TempletAdapter

    private var eventTime =""

    private var groomPhoto =""
    private var bridePhoto =""
    private var couplePhoto1 =""
    private var couplePhoto2 =""
    private var couplePhoto3 =""
    private var posterPhoto =""
    private var invitationPhoto =""
    private var husbandPhoto =""
    private var wifePhoto =""


    companion object {

        private const val EXTRAS_ID = "EXTRAS_ID"

        fun getStartIntent(context: Context, item: CategoryItem): Intent {
            return Intent(context, CreateEventActivity::class.java)
                .apply {
                    putExtra(EXTRAS_ID, item)
                }
        }

    }

    var groomLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                groomPhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgGroom)
            }

        }

    var brideLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                bridePhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgBride)
            }

        }
    var husbandLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                husbandPhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgHusband)
            }

        }

    var wifeLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                wifePhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgWife)
            }

        }

    var posterLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                posterPhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgPoster)
            }

        }
      var invitationLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                invitationPhoto = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgInvation)
            }

        }

    var couplePhoto1Launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                couplePhoto1 = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgCouplePhoto1)
            }

        }

    var couplePhoto2Launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                couplePhoto2 = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgCouplePhoto2)
            }

        }

    var couplePhoto3Launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                couplePhoto3 = FileHelper.getRealPathFromURI(this, it)
                Glide.with(this).load(it).into(binding.imgCouplePhoto3)
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        getIntentData()
        addClickListener()

        binding.btnCreateEvent.setOnClickListener {
            startActivity(TemplatePreviewActivity.getStartIntent(this, "https://devsinindia.com/","3" ))
        }

    }

    private fun getIntentData() {
        categoryItem = intent.serializable(EXTRAS_ID)
        categoryItem?.templates?.let { templetList.addAll(it) }
        setupTempletAdapter(templetList)

        categoryId = categoryItem?.id!!
        when (categoryId) {
            AppConstant.EVENT_CATEGORY_MARRIAGE -> {
                showMarriageUI()
            }

            AppConstant.EVENT_CATEGORY_BIRTHDAY -> {
                showBirthdayUI()
            }

            AppConstant.EVENT_CATEGORY_ANNIVERSARY -> {
                showAnniversaryUI()
            }

            AppConstant.EVENT_CATEGORY_BABY_SHOWER -> {
                showBabyShowerUI()
            }
        }
    }

    private fun setupTempletAdapter(list: List<TemplatesItem>) {
         adapter = TempletAdapter(list)
        { data, selectedPosition ->
            updateSelection(selectedPosition)
        }
        val recyclerView = binding.rvTemplet
        recyclerView.adapter = adapter



    }
    private fun updateSelection(selectedPosition: Int) {
        templetList.forEachIndexed { index, event ->
            event.isSelected = index == selectedPosition
        }
        adapter.notifyDataSetChanged()
    }
    /**
     * Add Multiple Click Listener
     */
    private fun addClickListener() {
        setOnClickListener(
            this,

            binding.btnCreateEvent,
            binding.groomUploadBtn,
            binding.brideUploadBtn,
            binding.husbandUploadBtn,
            binding.wifeUploadBtn,
            binding.posterUploadBtn,
            binding.invitationUploadBtn,
            binding.couple1UploadBtn,
            binding.couple2UploadBtn,
            binding.couple3UploadBtn,
            binding.edtEventDate

            )
    }

    /**
     * onClick is used for handle the click on this layout
     * @param view
     */
    override fun onClick(view: View?) {
        if (!preventMultipleClicks()) {
            return
        }
        when (view) {
            binding.groomUploadBtn -> groomLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.brideUploadBtn -> brideLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.husbandUploadBtn -> husbandLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.wifeUploadBtn -> wifeLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.posterUploadBtn -> posterLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.invitationUploadBtn -> invitationLauncher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.couple1UploadBtn -> couplePhoto1Launcher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.couple2UploadBtn -> couplePhoto2Launcher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.couple3UploadBtn -> couplePhoto3Launcher.launch( PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            binding.edtEventDate -> eventDateSelection()
           // binding.btnCreateEvent -> createEvent()


        }
    }

    private fun eventDateSelection() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(supportFragmentManager, "")

        datePicker.addOnPositiveButtonClickListener {
            val date = Util.millisToFormat(it)
            eventTime = Util.millisToHHmmss(it)
            binding.edtEventDate.setText(date)
            Logger.e("Selected Date",)
        }
    }

    private fun createEvent() {

        val title = binding.edtEventName.getValue()
        val venue = binding.edtEventVenue.getValue()
        val groomName = binding.edtGroomName.getValue()
        val brideName = binding.edtBrideName.getValue()
        val fatherName = binding.edtFatherName.getValue()
        val motherName = binding.edtMotherName.getValue()
        val babyName = binding.edtBabyName.getValue()
        val birthdayName = binding.edtBirthdayName.getValue()
        val birthdayAge = binding.edtBirthdayAge.getValue()
        val marriedYear = binding.edtMarriedYear.getValue()
        val expectedDate = binding.edtExpectedDate.getValue()
        val description = binding.edtWelcomeDesc.getValue()
        val husbandName = binding.edtHusbandName.getValue()
        val wifeName = binding.edtWifeName.getValue()

        val date = Util.convertLocalDateToServerDate(binding.edtEventDate.getValue())

        val selectedTemplet = templetList.find { it.isSelected }



        eventViewModal.createEvent(
            title = title,
            category_id = categoryId.toString(),
            venue = venue,
            templete_id = selectedTemplet?.id.toString(),
            welcome_description = description,
            event_date = date,
            groom_name = groomName,
            bride_name = brideName,
            father_name = fatherName,
            mother_name = motherName,
            baby_name = babyName,
            birthday_celebrant = birthdayName,
            how_old_celebrant = birthdayAge,
            husband_name = husbandName,
            wife_name = wifeName,
            how_long_married = marriedYear,
            groom_photo = groomPhoto,
            bride_photo = bridePhoto,
            husband_photo = husbandPhoto,
            wife_photo = wifePhoto,
            couple_photo1 = couplePhoto1,
            couple_photo2 = couplePhoto2,
            couple_photo3 = couplePhoto3,
            poster_photo = posterPhoto,
            invitation_card_photo = invitationPhoto,
            expected_date = expectedDate,
            gender = "1"
        )

    }

    private fun showMarriageUI() {
        hideViews(
            binding.inHusbandName,
            binding.inWifeName,
            binding.inMarriedYear,
            binding.inExpectedDate,
            binding.linGender,
            binding.linHusbandPhoto,
            binding.linWifePhoto,
            binding.linCouplePhoto1,
            binding.linCouplePhoto2,
            binding.linCouplePhoto3,
            binding.inFatherName,
            binding.inMotherName,
            binding.inBabyName,
            binding.linUploadInvitation,
            binding.inBirhtdayAge,
            binding.inBirhtdayName
        )
    }

    private fun showBirthdayUI() {
        hideViews(
            binding.inGroomName,
            binding.inBrideName,
            binding.inHusbandName,
            binding.inWifeName,
            binding.inMarriedYear,
            binding.inExpectedDate,
            binding.linGender,
            binding.linBrideUpload,
            binding.linGroomUpload,
            binding.linHusbandPhoto,
            binding.linWifePhoto,
            binding.linCouplePhoto1,
            binding.linCouplePhoto2,
            binding.linCouplePhoto3,
            binding.inFatherName,
            binding.inMotherName,
            binding.inBabyName
        )
    }

    private fun showAnniversaryUI() {
        hideViews(
            binding.inGroomName,
            binding.inBrideName,
            binding.inFatherName,
            binding.inMotherName,
            binding.inBabyName,
            binding.linUploadInvitation,
            binding.inBirhtdayAge,
            binding.inBirhtdayName,
            binding.inExpectedDate,
            binding.linGender,
            binding.inBabyName,
            binding.linBrideUpload,
            binding.linGroomUpload
        )
    }

    private fun showBabyShowerUI() {
        hideViews(
            binding.inGroomName,
            binding.inBrideName,
            binding.inHusbandName,
            binding.inWifeName,
            binding.linBrideUpload,
            binding.linGroomUpload,
            binding.linHusbandPhoto,
            binding.linWifePhoto,
            binding.linCouplePhoto1,
            binding.linCouplePhoto2,
            binding.linCouplePhoto3,
            binding.inBirhtdayAge,
            binding.inBirhtdayName,
            binding.inMarriedYear,
        )
    }

    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventViewModal.uiState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                           Toast.makeText(this@CreateEventActivity, "wow event created..", Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(this@CreateEventActivity, it.message)
                        }
                    }
                }
            }
        }
    }


}