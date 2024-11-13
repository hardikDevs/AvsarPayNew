package com.itcc.avasarpay.data.modal

import com.google.gson.annotations.SerializedName

data class MagicLinkModal(

    @field:SerializedName("data")
    val data: Data? = null,
) : BaseModal()

data class Data(

    @field:SerializedName("expires_at")
    val expiresAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("os_version")
    val osVersion: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("model")
    val model: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("platform")
    val platform: String? = null,

    @field:SerializedName("application_version")
    val applicationVersion: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)
