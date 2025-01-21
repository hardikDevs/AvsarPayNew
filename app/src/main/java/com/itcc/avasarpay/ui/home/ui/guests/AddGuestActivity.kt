package com.itcc.avasarpay.ui.home.ui.guests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.GuestIteam
import com.itcc.avasarpay.databinding.ActivityAddGuestBinding
import com.itcc.avasarpay.utils.Util.preventMultipleClicks
import com.itcc.avasarpay.utils.Util.setOnClickListener
import com.itcc.stonna.utils.getValue
import com.itcc.stonna.utils.isValidEmail
import com.itcc.stonna.utils.showSnackBar
import com.itcc.stonna.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class AddGuestActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddGuestBinding

    private val guestViewModel: GuestViewModel by viewModels()

    private var eventId = "0"
    private var guestType = "0"


    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, eventId: String): Intent {
            return Intent(context, AddGuestActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, eventId)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
         eventId = intent.getStringExtra(EXTRAS_TITLE).toString()
        guestViewModel.getEventGuestList(eventId.toString())
        setupObserver()
        addClickListener()


        binding.rg.setOnCheckedChangeListener { group, checkedId ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton

            if (checkedId == R.id.rbFamily) {
                guestType = "1"
            } else {
                guestType = "2"
            }


        }
    }

    /**
     * Add Multiple Click Listener
     */
    private fun addClickListener() {
        setOnClickListener(
            this,
            binding.linAddGuest,
        )
    }

    /**
     * onClick is used for handle the click on this layout
     * @param view
     */
    override fun onClick(view: View?) {
        if (!preventMultipleClicks()) {
            return
        }
        when (view) {
            binding.linAddGuest -> if (isValid()) contactApiCall()

            /*   binding.facebook -> googleLogin()
               binding.google -> googleLogin()*/

        }
    }

    private fun setupGuestAdapter(list: List<GuestIteam>) {
       val adapter = GuestAdapter(list)
        val recyclerView = binding.rvGuest
        recyclerView.adapter = adapter

        adapter.itemClickListener = {
            //  startActivity(JobDetailsActivity.getStartIntent(requireContext(), it.id!!))
        }

    }

    private fun isValid(): Boolean {

        if (binding.edtGuestName.getValue().isEmpty()) {
            binding.inGuestName.error = "Please enter name"
            return false
        }

        if (binding.edtGuestEmail.text.toString().isEmpty()) {
            binding.inGuestEmail.error = "Please enter email address"
            return false
        }
        if (!isValidEmail(binding.edtGuestEmail.getValue())) {
            binding.inGuestEmail.error = "Please enter valid email address"
            return false
        }
        if (binding.edtGuestNumber.text.toString().isEmpty()) {
            binding.inGuestPhone.error = "Please enter phone number"
            return false
        }
        if (guestType == "0") {
            showToast("Please select guest type")
            return false
        }


        return true
    }

    private fun contactApiCall() {

        val phone = binding.edtGuestNumber.text.toString()
        val name = binding.edtGuestName.text.toString()
        val email = binding.edtGuestEmail.text.toString()

        var jsonArray = JSONArray()
        var jsonObject = JSONObject()

        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("phone", phone)
        jsonObject.put("guest_type", guestType)

        jsonArray.put(jsonObject)



        guestViewModel.createContacts(eventId, jsonArray)

        /*  profileViewModal.register(
              registerReq
          )*/
    }

    /**
     * Get Flow Event
     */
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                guestViewModel.uiContacts.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            showToast("Wow Guest Added to event")
                            binding.edtGuestName.text?.clear()
                            binding.edtGuestNumber.text?.clear()
                            binding.edtGuestEmail.text?.clear()
                            binding.rg.clearCheck()
                            guestViewModel.getEventGuestList("3")
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                guestViewModel.uiState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            setupGuestAdapter(it.data.data)
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(this@AddGuestActivity, it.message)
                        }
                    }
                }
            }
        }
    }


}