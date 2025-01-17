package com.itcc.avasarpay.ui.home.ui.event.contact

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcc.avasarpay.base.BaseActivity
import com.itcc.avasarpay.data.modal.Contact
import com.itcc.avasarpay.databinding.ActivityImportContactBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImportContactActivity : BaseActivity() {

    private lateinit var binding: ActivityImportContactBinding

    private val contactViewModel : ContactViewModel by viewModels()
    private var contacts = mutableListOf<Contact>()
    private lateinit var contactAdapter: ContactAdapter
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

        fun getStartIntent(context: Context, country: String): Intent {
            return Intent(context, ImportContactActivity::class.java)
                .apply {
                    putExtra(EXTRAS_TITLE, country)
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        contactAdapter = ContactAdapter(){
                position ->
            contacts[position].isSelected = !contacts[position].isSelected
            contactAdapter.notifyItemChanged(position)
        }

        // Setup RecyclerView
        binding.rvContact.apply {
            layoutManager = LinearLayoutManager(this@ImportContactActivity)
            adapter = contactAdapter
        }

        // Observe Contacts
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactViewModel.contacts.collect { contactsList ->
                    contactAdapter.submitList(contactsList)
                    contacts= contactsList.toMutableList()
                }
            }
        }

        // Observe Loading State
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactViewModel.isLoading.collect { isLoading ->
                        if (isLoading) showProgressbar() else hideProgressbar()
                }
            }
        }

        // Request Contacts Permission
        requestContactPermission()

        binding.imgDone.setOnClickListener {

            Log.d("selected ", "${getSelectedContacts().size}")
        }

    }

    fun getSelectedContacts(): List<Contact> {
        return contacts.filter { it.isSelected }
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



