package com.itcc.avasarpay.ui.home.ui.event.contact

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcc.avasarpay.R
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.base.UiState
import com.itcc.avasarpay.data.modal.Contact
import com.itcc.avasarpay.databinding.ActivityImportContactBinding
import com.itcc.avasarpay.ui.home.ui.guests.GuestViewModel
import com.itcc.stonna.utils.showSnackBar
import com.itcc.stonna.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class ImportContactActivity : BaseActivity() {

    private lateinit var binding: ActivityImportContactBinding

    private val guestViewModel: GuestViewModel by viewModels()
    private val contactViewModel: ContactViewModel by viewModels()
    private var contacts = mutableListOf<Contact>()
    private lateinit var contactAdapter: ContactAdapter
    private var eventId = "0"

    private val contactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            contactViewModel.loadContacts()
        } else {
            Toast.makeText(
                this,
                "Contact permission is required",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        private const val EXTRAS_TITLE = "EXTRAS_TITLE"

        fun getStartIntent(context: Context, eventId: String): Intent {
            return Intent(context, ImportContactActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, eventId)
                }
        }

    }

    override fun onPause() {
        super.onPause()
        contacts.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        contacts.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Request Contacts Permission
        requestContactPermission()
        eventId = intent.getStringExtra(EXTRAS_TITLE).toString()
        setupObserver()

        contactAdapter = ContactAdapter() { position ->
            contacts[position].isSelected = !contacts[position].isSelected
            contactAdapter.notifyItemChanged(position)
        }

        // Setup RecyclerView
        binding.rvContact.apply {
            layoutManager = LinearLayoutManager(this@ImportContactActivity)
            adapter = contactAdapter
        }




        binding.include.imgNotification.setImageResource(R.drawable.ic_done)
        binding.include.imgNotification.setOnClickListener {

           if (getSelectedContacts().isNotEmpty())
            contactApiCall()
            else showToast("Please select contact")
        }

    }

    private fun getSelectedContacts(): List<Contact> {
        return contacts.filter { it.isSelected }
    }

    private fun contactApiCall() {
        var jsonArray = JSONArray()
        getSelectedContacts().forEach {
            var jsonObject = JSONObject()

            jsonObject.put("name", it.name)
            jsonObject.put("email", it.emails[0])
            jsonObject.put("phone", it.phoneNumbers[0])
            jsonObject.put("guest_type", "2")

            jsonArray.put(jsonObject)
        }

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
                            contacts.clear()
                            finish()
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(
                                this@ImportContactActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                contactViewModel.uiState.collectLatest {
                    when (it) {
                        is UiState.Success -> {
                            hideProgressbar()
                            contactAdapter.submitList(it.data.contactList)
                            contacts = it.data.contactList.toMutableList()
                        }

                        is UiState.Loading -> {
                            showProgressbar()
                        }

                        is UiState.Idle -> {
                        }

                        is UiState.Error -> {
                            hideProgressbar()
                            binding.root.showSnackBar(
                                this@ImportContactActivity,
                                it.message
                            )
                        }
                    }
                }
            }
        }
    }




    private fun requestContactPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                contactViewModel.loadContacts()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                AlertDialog.Builder(this)
                    .setTitle("Contact Permission")
                    .setMessage("This app needs access to your contacts to display them.")
                    .setPositiveButton("OK") { _, _ ->
                        contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    .create()
                    .show()
            }

            else -> {
                contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
}



