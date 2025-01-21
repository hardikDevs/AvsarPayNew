package com.itcc.avasarpay.ui.home.ui.event

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
class CreateEventSelectCategoryActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateEventStep1Binding

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private var categoryId = "0"
    private val list: MutableList<CategoryItem> = mutableListOf()
    private lateinit var adapter: CategorySelectionAdapter

    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, CreateEventSelectCategoryActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        dashboardViewModel.getCategoryList()

        binding.button.setOnClickListener {
            val selectedEvent = list.find { it.isSelected }
            if (selectedEvent == null) {
                Toast.makeText(this, "Please select an event", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Selected: ${selectedEvent?.name}", Toast.LENGTH_SHORT).show()
            startActivity(
                CreateEventActivity.getStartIntent(
                    this,
                    selectedEvent
                )
            )
        }

    }

    private fun setupCategoryAdapter(categoryItem: List<CategoryItem>) {
        adapter = CategorySelectionAdapter(categoryItem) { data, selectedPosition ->
            updateSelection(selectedPosition)
        }
        val recyclerView = binding.rvCategory
        recyclerView.adapter = adapter
    }

    private fun updateSelection(selectedPosition: Int) {
        list.forEachIndexed { index, event ->
            event.isSelected = index == selectedPosition
        }
        adapter.notifyDataSetChanged()
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
                            list.clear()
                            list.addAll(it.data.item)
                            setupCategoryAdapter(it.data.item)
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(
                                this@CreateEventSelectCategoryActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
    }


}