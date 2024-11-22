package com.itcc.avasarpay.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itcc.avasarpay.R

import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.databinding.SplashScreenBinding
import com.itcc.avasarpay.ui.auth.LoginActivity
import com.itcc.avasarpay.ui.home.DashboardActivity


import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.ui.auth.LoginViewModal
import com.itcc.avasarpay.ui.auth.RegisterActivity
import com.itcc.stonna.utils.SessionManager
import com.itcc.stonna.utils.showSnackBar
import com.itcc.stonna.utils.showToast
import kotlinx.coroutines.launch


/**
 *Created By Sunny on 05-03-2024.
 */
@AndroidEntryPoint
class SplashScreen : BaseActivity() {

    private lateinit var binding: SplashScreenBinding
    private val loginViewModal: LoginViewModal by viewModels()

    /** Duration of wait  */
    private val SPLASH_DISPLAY_LENGTH = 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        /* @Suppress("DEPRECATION")
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
             window.insetsController?.hide(WindowInsets.Type.statusBars())
         } else {
             window.setFlags(
                 WindowManager.LayoutParams.FLAG_FULLSCREEN,
                 WindowManager.LayoutParams.FLAG_FULLSCREEN)
         }*/

        setupObserver()
        handleIncomingLink()


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
                            showToast(it.data.token.toString())
                            session.user = it.data
                            session.storeDataByKey(SessionManager.KEY_ACCESS_TOKEN, it.data.token.toString())
                            session.isLoggedIn = true
                            if (it.data.data?.name != null) {

                                startActivity(
                                    DashboardActivity.getStartIntent(
                                        this@SplashScreen,
                                        "test"
                                    )
                                )
                            } else
                                startActivity(
                                    RegisterActivity.getStartIntent(
                                        this@SplashScreen,
                                        it.data.data?.email.toString()
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
                            binding.root.showSnackBar(this@SplashScreen, it.message)
                        }
                    }
                }
            }
        }


    }

    private fun handleIncomingLink() {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            // Parse the URI
            when {
                uri?.path?.startsWith("/magic/") == true -> {
                    val token = uri.lastPathSegment
                    handleLogin(token)
                }

                else -> handleRedirection()

                /*  uri?.path?.startsWith("/category/") == true -> {
                      val categoryId = uri.lastPathSegment
                      openCategory(categoryId)
                  }*/
                // Add more path handlers as needed
            }
        }

        if (intent?.action == Intent.ACTION_MAIN) {
            handleRedirection()
        }
    }

    private fun handleRedirection() {

        Handler(Looper.getMainLooper()).postDelayed({
            if (session.isLoggedIn && session.user.data?.name!=null)
                startActivity(DashboardActivity.getStartIntent(this, "test"))

           else if (session.isLoggedIn && session.user.data?.name==null)
                startActivity(RegisterActivity.getStartIntent(this, session.user.data?.email.toString()))
            else {
                startActivity(LoginActivity.getStartIntent(this, "test"))
            }

            finish()

        }, SPLASH_DISPLAY_LENGTH)

    }

    private fun handleLogin(token: String?) {
        loginViewModal.login(token.toString())
    }
}