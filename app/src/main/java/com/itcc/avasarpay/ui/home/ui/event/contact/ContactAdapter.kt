package com.itcc.avasarpay.ui.home.ui.event.contact

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itcc.avasarpay.data.modal.Contact
import com.itcc.avasarpay.databinding.GuestSingleBinding
import com.itcc.avasarpay.databinding.RowLocalContactListBinding
import java.util.Random

/**
 *Created By Sunny on 13-12-2024.
 */
class ContactAdapter( private val onItemClick: (Int) -> Unit):  ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    class ContactViewHolder(private val binding: RowLocalContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Contact, onItemClick: (Int) -> Unit) {
            binding.apply {
                binding.name.text = data.name
                if (data.emails.isNotEmpty())
                binding.email.text = data.emails[0]
                else binding.email.text = "No Email"
                if (data.phoneNumbers.isNotEmpty())
                binding.number.text = data.phoneNumbers[0]
                else binding.number.text = "No Number"

                binding.action.isChecked = data.isSelected


                val r = Random()
                val red: Int = r.nextInt(255 - 0 + 1) + 0
                val green: Int = r.nextInt(255 - 0 + 1) + 0
                val blue: Int = r.nextInt(255 - 0 + 1) + 0

                binding.firstLetter.text = data.name?.substring(0, 1)
                binding.cardLatter.setCardBackgroundColor(Color.rgb(red, green, blue))


            itemView.setOnClickListener {
                    onItemClick(absoluteAdapterPosition)
                }
              binding.action.setOnClickListener {
                    onItemClick(absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = RowLocalContactListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem == newItem
    }
}