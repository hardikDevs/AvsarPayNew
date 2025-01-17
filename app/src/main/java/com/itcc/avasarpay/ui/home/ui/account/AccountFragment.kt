package com.itcc.avasarpay.ui.home.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itcc.avasarpay.base.BaseFragment
import com.itcc.avasarpay.databinding.FragmentAccountBinding
import com.itcc.avasarpay.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): AccountFragment {
            return AccountFragment().apply {
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
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = session.user?.data
        binding.userProfileName.text = user?.name.toString()
        binding.userProfileEmail.text = user?.email
        binding.userProfileNum.text = user?.phone

        binding.logout.setOnClickListener {
            logout()
        }

        binding.profLin.setOnClickListener {
            startActivity(ProfileActivity.getStartIntent(requireContext(), ""))
        }

    }

    fun logout() {
        session.clearSession()
        startActivity(LoginActivity.getStartIntent(requireContext(), "test"))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}