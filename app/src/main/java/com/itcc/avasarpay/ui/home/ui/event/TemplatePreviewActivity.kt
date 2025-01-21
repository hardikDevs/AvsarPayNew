package com.itcc.avasarpay.ui.home.ui.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.databinding.ActivityTempletPreviewBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.avasarpay.ui.home.ui.guests.AddGuestActivity
import com.itcc.avasarpay.ui.home.ui.guests.InviteGuestActivity
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TemplatePreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityTempletPreviewBinding

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private var eventId = "0"


    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"
        private const val EXTRAS_ID = "EXTRAS_ID"

        fun getStartIntent(context: Context, url: String, eventId:String): Intent {
            return Intent(context, TemplatePreviewActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, url)
                    putExtra(EXTRAS_ID, eventId)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempletPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        val url = intent.getStringExtra(EXTRAS_TITLE)
        val eventId= intent.getStringExtra(EXTRAS_ID)
        binding.webview.loadUrl(url.toString())
        binding.btnAddGust.setOnClickListener {
            startActivity(InviteGuestActivity.getStartIntent(this, eventId.toString()))
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
                                this@TemplatePreviewActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
    }


}