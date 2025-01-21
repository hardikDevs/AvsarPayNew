package com.itcc.avasarpay.data.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import com.itcc.avasarpay.data.modal.Contact
import com.itcc.avasarpay.data.modal.LocalContactModal
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *Created By Sunny on 13-12-2024.
 */
@Singleton
class ContactRepository @Inject constructor(@ApplicationContext val context: Context) {
    val contactList = mutableListOf<Contact>()

    fun clearContacts() {
        contactList.clear()
    }

   suspend  fun fetchContacts(): Flow<LocalContactModal> {
        // Define the columns you want to retrieve for contacts
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        // Content Resolver to query the Contacts Content Provider
        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null, // No selection
            null, // No selectionArgs
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Sort by name ascending
        )

      withContext(Dispatchers.IO){
          cursor?.let {
              while (it.moveToNext()) {
                  val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                  val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                  // Check if the contact has a phone number
                  val hasPhoneNumber =
                      it.getString(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                  if (hasPhoneNumber == "1") {
                      // Query phone numbers for this contact
                      val phonesCursor: Cursor? =  context.contentResolver.query(
                          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                          arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                          ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                          arrayOf(id),
                          null
                      )

                      // Get phone numbers for the contact
                      val phoneNumbers = mutableListOf<String>()
                      phonesCursor?.let { phoneCursor ->
                          while (phoneCursor.moveToNext()) {
                              val phoneNumber = phoneCursor.getString(
                                  phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                              )
                              phoneNumbers.add(phoneNumber.filter { !it.isWhitespace() }.replace("+91", ""))
                          }
                          phoneCursor.close()
                      }

                      // Query emails for this contact
                      val emailsCursor: Cursor? =  context.contentResolver.query(
                          ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                          arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                          ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                          arrayOf(id),
                          null
                      )

                      val emails = mutableListOf<String>()
                      emailsCursor?.let { emailCursor ->
                          while (emailCursor.moveToNext()) {
                              val email = emailCursor.getString(
                                  emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                              )
                              emails.add(email.filter { !it.isWhitespace() })
                          }
                          emailCursor.close()
                      }

                      // Add the contact with the phone number and email to the list
                      contactList.add(
                          Contact(
                              id.toInt(),
                              name,
                              phoneNumbers ,
                              emails
                          )
                      )
                  }
              }
              it.close()
          }
      }



        // Handle empty contact list
        if (contactList.isEmpty()) {
            Toast.makeText(context, "No contacts found", Toast.LENGTH_SHORT).show()
        }

         val modal = LocalContactModal(contactList)

        return flow {
            emit(modal)
        }
    }






        // Do something with the contact list, like setting it to a RecyclerView
        // Example: binding.recyclerView.adapter?.notifyDataSetChanged()
}