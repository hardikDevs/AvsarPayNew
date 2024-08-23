package com.itcc.avasarpay.data.modal

data class GuestModal(
	val item: MutableList<GuestItem> = mutableListOf(),
	val message: String? = null,
	val status: Boolean? = null
)

data class GuestItem(
	val phone: String? = null,
	val name: String? = null,
	val createdAt: String? = null,
	val id: Int? = null,
	val giftAmount: Int? = null,
	val email: String? = null
)

