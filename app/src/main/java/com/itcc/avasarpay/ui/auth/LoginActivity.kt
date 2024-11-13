package com.itcc.avasarpay.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.itcc.avasarpay.BuildConfig
import com.itcc.avasarpay.R

import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.databinding.ActivityLoginBinding
import com.itcc.avasarpay.ui.home.DashboardActivity

import com.itcc.avasarpay.utils.Util.preventMultipleClicks
import com.itcc.avasarpay.utils.Util.setOnClickListener
import com.itcc.stonna.utils.Logger
import com.itcc.stonna.utils.getValue
import com.itcc.stonna.utils.isValidEmail
import com.itcc.stonna.utils.showSnackBar
import com.itcc.stonna.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 *Created By Sunny on 05-03-2024.
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val magicLinkViewModal: MagicLinkViewModal by viewModels()
    private var fcmToken = ""


    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, LoginActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        addClickListener()
        FirebaseApp.initializeApp(this)


        // Retrieving the FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.e("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // fetching the token
            fcmToken = task.result

            Logger.d("FCM Token", fcmToken.toString())
            // toast to show  message
            Toast.makeText(
                baseContext,
                "Firebase Generated Successfully and saved to realtime database",
                Toast.LENGTH_SHORT
            ).show()
        })

    }


    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                magicLinkViewModal.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            showToast(it.data.message.toString())
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(this@LoginActivity, it.message)
                        }
                    }
                }
            }
        }


    }

    /**
     * Add Multiple Click Listener
     */
    private fun addClickListener() {
        setOnClickListener(
            this,

            binding.button,

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
            binding.button -> if (validation()) sendMagicLink()


        }
    }


    private fun sendMagicLink() {
        val osVersion = Build.VERSION.RELEASE
        val applicationVersion = BuildConfig.VERSION_CODE.toString()
        magicLinkViewModal.sendMagicLink(
            binding.email.getValue(),
            fcmToken,
            "Android",
            applicationVersion,
            osVersion,

            )
    }

    /**
     * Check Input Validation
     **/
    private fun validation(): Boolean {
        when {
            binding.email.text.toString().isEmpty() -> {
                binding.emailMain.error = "Please enter email address"
                return false
            }

            !isValidEmail(binding.email.getValue()) -> {
                binding.emailMain.error = "Please enter valid email address"
                return false
            }


            else -> {
                return true
            }

        }

    }
}