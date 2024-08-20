package com.itcc.avasarpay.ui

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


/**
 *Created By Sunny on 05-03-2024.
 */
@AndroidEntryPoint
class SplashScreens : BaseActivity() {

    private lateinit var binding: SplashScreenBinding

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
        Handler(Looper.getMainLooper()).postDelayed({

            if (session.isLoggedIn)
                startActivity(DashboardActivity.getStartIntent(this, "test"))
            else{
                startActivity(LoginActivity.getStartIntent(this, "test"))
            }

            finish()

            }, SPLASH_DISPLAY_LENGTH)
        }



}