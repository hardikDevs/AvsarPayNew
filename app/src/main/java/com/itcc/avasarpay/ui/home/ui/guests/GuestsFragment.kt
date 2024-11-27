package com.itcc.avasarpay.ui.home.ui.guests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itcc.avasarpay.base.BaseFragment
import com.itcc.avasarpay.databinding.FragmentGuestMainBinding
import dagger.hilt.android.AndroidEntryPoint

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