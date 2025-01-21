package com.itcc.avasarpay.data.modal

import com.google.gson.annotations.SerializedName

data class AllGuestModal(

	@field:SerializedName("guests_contacts")
	val guestsContacts: List<GuestIteam> = mutableListOf(),

	@field:SerializedName("friends_contacts")
	val friendsContacts: List<GuestIteam> = mutableListOf(),

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)


