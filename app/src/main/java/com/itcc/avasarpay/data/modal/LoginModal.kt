package com.itcc.avasarpay.data.modal

import com.google.gson.annotations.SerializedName

data class LoginModal(

	@field:SerializedName("data")
	val data: User? = null,

	@field:SerializedName("token")
	val token: String? = null
) :BaseModal()

data class User(

	@field:SerializedName("role")
	val role: Int? = null,

	@field:SerializedName("wallet")
	val wallet: String? = null,

	@field:SerializedName("os_version")
	val osVersion: String? = null,

	@field:SerializedName("about")
	val about: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("platform")
	val platform: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("uid")
	val uid: Any? = null,

	@field:SerializedName("social_id")
	val socialId: Any? = null,

	@field:SerializedName("profile_image")
	val profileImage: Any? = null,

	@field:SerializedName("expires_at")
	val expiresAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: Any? = null,

	@field:SerializedName("phone_verified_at")
	val phoneVerifiedAt: Any? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("application_version")
	val applicationVersion: String? = null
)
