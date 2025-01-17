package com.itcc.avasarpay.ui.home.ui.guests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.databinding.ActivityInviteGuestsBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.avasarpay.ui.home.ui.event.contact.ImportContactActivity
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InviteGuestActivity : BaseActivity() {

    private lateinit var binding: ActivityInviteGuestsBinding

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private var categoryId = "0"


    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, InviteGuestActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteGuestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupObserver()
        binding.linAddGuest.setOnClickListener {
            startActivity(AddGuestActivity.getStartIntent(this, ""))
        }
        binding.linImportContact.setOnClickListener {
            startActivity(ImportContactActivity .getStartIntent(this, ""))
        }
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
                                this@InviteGuestActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
    }


}