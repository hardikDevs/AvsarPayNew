package com.itcc.avasarpay.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.databinding.ActivityRegisterBinding
import com.itcc.avasarpay.ui.home.DashboardActivity
import com.itcc.avasarpay.utils.FileHelper
import com.itcc.avasarpay.utils.Util.preventMultipleClicks
import com.itcc.avasarpay.utils.Util.setOnClickListener
import com.itcc.stonna.utils.getValue
import com.itcc.stonna.utils.isValidEmail
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private val profileViewModal: ProfileViewModal by viewModels()
    private var filename = ""
    private var path = ""

    companion object {

        private const val EXTRAS_EMAIl = "EXTRAS_EMAIL"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, RegisterActivity::class.java)
                .apply {
                    putExtra(EXTRAS_EMAIl, country)
                }
        }

    }

    var launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {

            if (it != null) {
                // val mimeType = it.getMimeType(this)
                path = FileHelper.getRealPathFromURI(this, it)
                filename = path.substring(path.lastIndexOf("/") + 1)
                Glide.with(this).load(it).into(binding.img)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentAndFetchData()
        setupObserver()
        addClickListener()
    }

    private fun getIntentAndFetchData() {
        val email = intent.getStringExtra(EXTRAS_EMAIl)
        email?.let {

            binding.email.setText(email)
        }
    }

    /**
     * Add Multiple Click Listener
     */
    private fun addClickListener() {
        setOnClickListener(
            this,
            binding.button,
            binding.img,
            binding.btnLogout
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
            binding.button -> if (isValidRegister()) registerApiCall()
            binding.img -> {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            binding.btnLogout -> {
                session.clearSession()
                startActivity(
                    LoginActivity.getStartIntent(
                        this@RegisterActivity
                    )
                )
                finish()
            }
            /*   binding.facebook -> googleLogin()
               binding.google -> googleLogin()*/

        }
    }


    private fun isValidRegister(): Boolean {

        if (binding.name.text.toString().isEmpty()) {
            binding.nameMain.error = "Please enter name"
            return false
        }

        if (binding.email.text.toString().isEmpty()) {
            binding.emailMain.error = "Please enter email address"
            return false
        }
        if (!isValidEmail(binding.email.getValue())) {
            binding.emailMain.error = "Please enter valid email address"
            return false
        }
        if (binding.phone.text.toString().isEmpty()) {
            binding.phoneMain.error = "Please enter phone number"
            return false
        }


        return true
    }

    private fun registerApiCall() {

        val phone = binding.phone.text.toString()
        val name = binding.name.text.toString()



        val id = session.user?.data?.id

        profileViewModal.updateProfile(id!!, name, phone, path, filename)

        /*  profileViewModal.register(
              registerReq
          )*/
    }

    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {

                profileViewModal.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            session.isLoggedIn = true
                            session.user = it.data
                            startActivity(
                                DashboardActivity.getStartIntent(
                                    this@RegisterActivity,
                                    "test"
                                )
                            )
                            finish()

                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(this@RegisterActivity, it.message)
                        }
                    }
                }
            }



    }
}