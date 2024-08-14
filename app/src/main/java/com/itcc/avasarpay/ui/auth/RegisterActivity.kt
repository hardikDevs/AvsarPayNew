package com.itcc.avasarpay.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.BuildConfig
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.RegisterReq
import com.itcc.avasarpay.databinding.ActivityLoginBinding
import com.itcc.avasarpay.databinding.ActivityRegisterBinding
import com.itcc.avasarpay.ui.home.HomeActivity
import com.itcc.avasarpay.utils.Util.preventMultipleClicks
import com.itcc.avasarpay.utils.Util.setOnClickListener
import com.itcc.stonna.utils.getValue
import com.itcc.stonna.utils.isValidEmail
import com.itcc.stonna.utils.isValidPasswordFormat
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModal: RegisterViewModal by viewModels()

    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, RegisterActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupObserver()
        addClickListener()
    }

    /**
     * Add Multiple Click Listener
     */
    private fun addClickListener() {
        setOnClickListener(
            this,
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
            binding.button -> if (isValidRegister()) registerApiCall()
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

        if (binding.password.text.toString().isEmpty()) {
            binding.passwordMain.error = "Please enter password"
            return false
        }
        if (isValidPasswordFormat(binding.password.toString())) {
            binding.passwordMain.error =
                "Use 8 or more characters with a mix of letters, numbers and symbols."
            binding.passwordReq.visibility = View.GONE
            return false
        }
        if (binding.confpassword.text.toString().isEmpty()) {
            binding.conpasswordMain.error = "Please enter confirm password"
            binding.passwordReq.visibility = View.VISIBLE
            return false
        }

        if (!binding.confpassword.text.toString().equals(binding.password.text.toString())) {
            binding.conpasswordMain.error = "Password and Confirm password must be same."
            binding.passwordReq.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun registerApiCall() {
        val osVersion = Build.VERSION.RELEASE
        val applicationVersion = BuildConfig.VERSION_CODE.toString()
        val registerReq = RegisterReq()
        registerReq.email = binding.email.text.toString()
        registerReq.password = binding.password.text.toString()
        registerReq.phone = binding.phone.text.toString()
        registerReq.name = binding.name.text.toString()
        // registerReq.about = binding.info.text.toString()
        registerReq.password_confirmation = binding.confpassword.text.toString()
        registerReq.platform = "android"
        registerReq.application_version = applicationVersion
        registerReq.os_version = osVersion
        registerReq.type = "2" // 0= Gmail,1= Fb , 2 = Custom
        registerViewModal.register(
            registerReq
        )
    }

    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModal.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            session.isLoggedIn = true
                            session.user = it.data.item
                            startActivity(
                                HomeActivity.getStartIntent(
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
}