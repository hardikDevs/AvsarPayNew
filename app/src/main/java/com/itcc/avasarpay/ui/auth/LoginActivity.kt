package com.itcc.avasarpay.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 *Created By Sunny on 05-03-2024.
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModal: LoginViewModal by viewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()
    }


    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModal.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                             session.user = it.data.item
                            session.isLoggedIn = true
                            startActivity(DashboardActivity.getStartIntent(this@LoginActivity, "test"))
                            finish()

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
            binding.register,
            binding.button,
            binding.facebook,
            binding.google
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
            binding.button -> if (validation()) loginApiCall()
            binding.register -> goToRegisterScreen()
            binding.facebook -> googleLogin()
            binding.google -> googleLogin()

        }
    }


    private fun googleLogin() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(getString(R.string.web_client_id))
            .setAutoSelectEnabled(true)
            .setNonce(null)
            .build()

        val getCredRequest: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()


        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            // try {
            val result = credentialManager.getCredential(
                // Use an activity-based context to avoid undefined system UI
                // launching behavior.
                context = this@LoginActivity,
                request = getCredRequest
            )
            handleSignIn(result)
            /*  } catch (e : GetCredentialException) {
                  Log.e("error", "Unexpected type of credential")
              }*/
        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential
        Logger.i(credential.data.toString())
        //   val credential2 = GoogleAuthProvider.getCredential(result.idToken, null)
        /*  firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
              if (task.isSuccessful) {
                 *//* Log.e(TAG, account.email.toString())
                AppLog.e(TAG, account.displayName.toString())*//*
                *//*SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()*//*
            }
        }*/
        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
                Logger.e(responseJson)
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
                Logger.e(username)
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        Logger.e(googleIdTokenCredential.toString())
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.
                        /* GoogleIdTokenVerifier verifier = ... // see validation instructions
                         GoogleIdToken idToken = verifier.verify(idTokenString);
                         // To get a stable account identifier (e.g. for storing user data),
                         // use the subject ID:
                         idToken.getPayload().getSubject()*/
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("error", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("error", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("error", "Unexpected type of credential")
            }
        }
    }

    private fun loginApiCall() {
        val osVersion = Build.VERSION.RELEASE
        val applicationVersion = BuildConfig.VERSION_CODE.toString()
        loginViewModal.login(
            binding.email.getValue(),
            binding.password.getValue(),
            "Custom",
            "android",
            osVersion,
            applicationVersion
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

            binding.password.text.toString().isEmpty() -> {
                binding.password.error = "Please enter password"
                return false
            }

            binding.password.text.toString().length < 6 -> {
                binding.password.error = "The password must be at least 6 characters."
                return false
            }

            else -> {
                return true
            }

        }

    }

    private fun goToRegisterScreen() {
        startActivity(RegisterActivity.getStartIntent(this, "test"))
    }

    private fun getIntentAndFetchData() {
        val title = intent.getStringExtra(EXTRAS_TITLE)
        title?.let {

        }
    }
}