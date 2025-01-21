package com.itcc.avasarpay.data.modal

import com.google.gson.annotations.SerializedName

data class GuestModal(

	@field:SerializedName("data")
	val data: List<GuestIteam> = mutableListOf(),

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class GuestIteam(

	@field:SerializedName("stripe_customer_id")
	val stripeCustomerId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("guest_type")
	val guestType: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
