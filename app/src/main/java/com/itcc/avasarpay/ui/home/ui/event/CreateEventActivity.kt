package com.itcc.avasarpay.ui.home.ui.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.databinding.ActivityCreateEventBinding
import com.itcc.avasarpay.databinding.ActivityDashboardBinding
import com.itcc.avasarpay.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEventActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, CreateEventActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}