package com.itcc.avasarpay.ui.home.ui.guests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.CategoryItem
import com.itcc.avasarpay.databinding.ActivityAddGuestBinding
import com.itcc.avasarpay.databinding.ActivityCreateEventBinding
import com.itcc.avasarpay.databinding.ActivityCreateEventStep1Binding
import com.itcc.avasarpay.databinding.ActivityDashboardBinding
import com.itcc.avasarpay.databinding.ActivityProfileBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.CategoryAdapter
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddGuestActivity : BaseActivity() {

    private lateinit var binding: ActivityAddGuestBinding

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private var categoryId = "0"


    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, AddGuestActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupObserver()

    }


    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.uiState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(
                                this@AddGuestActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
    }


}