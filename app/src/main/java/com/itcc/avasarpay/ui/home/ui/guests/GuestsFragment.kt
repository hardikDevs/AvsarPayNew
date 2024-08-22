package com.itcc.avasarpay.ui.home.ui.guests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.base.BaseFragment
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.CateListData
import com.itcc.avasarpay.databinding.FragmentGuestBinding
import com.itcc.avasarpay.databinding.FragmentGuestMainBinding
import com.itcc.avasarpay.databinding.FragmentNotificationsBinding
import com.itcc.avasarpay.ui.home.ui.dashboard.CategoryAdapter
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardFragment
import com.itcc.avasarpay.ui.home.ui.dashboard.DashboardViewModel
import com.itcc.stonna.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GuestsFragment : BaseFragment() {

    private var _binding: FragmentGuestMainBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): GuestsFragment {
            return GuestsFragment().apply {
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


        _binding = FragmentGuestMainBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter(parentFragmentManager)
        adapter.addFragment(AllGuestFragment(), "All Guests")
        adapter.addFragment(FrindGuestFragment(), "Friends & Family")

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}