package com.itcc.avasarpay.ui.home.ui.guests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcc.avasarpay.base.BaseFragment
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.CateListData
import com.itcc.avasarpay.data.modal.ContactData
import com.itcc.avasarpay.data.modal.GuestItem
import com.itcc.avasarpay.databinding.FragmentDashboardBinding
import com.itcc.avasarpay.databinding.FragmentGuestBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.CategoryAdapter
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllGuestFragment : BaseFragment() {

    private var _binding: FragmentGuestBinding? = null
    private val guestsViewModel: GuestViewModel by viewModels()
    private lateinit var adapter: AllGuestAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): AllGuestFragment {
            return AllGuestFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentGuestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guestsViewModel.getGuestList()
        setupObserver()
    }

    private fun searchContact(){
        binding.contactSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                adapter.filter.filter(newText)

                return true
            }

        })
    }

    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                guestsViewModel.uiState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            setupGuestAdapter(it.data.item)
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(requireContext(), it.message)
                        }
                    }
                }
            }
        }
    }

    private fun setupGuestAdapter(jobListItems: MutableList<GuestItem>) {
         adapter = AllGuestAdapter(jobListItems)
        val recyclerView = binding.contactList
        recyclerView.adapter = adapter

        adapter.itemClickListener = {
            //  startActivity(JobDetailsActivity.getStartIntent(requireContext(), it.id!!))
        }
        searchContact()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}