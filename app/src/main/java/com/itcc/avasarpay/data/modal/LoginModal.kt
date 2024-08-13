package com.itcc.avasarpay.data.modal


import com.google.gson.annotations.SerializedName

data class LoginModal(

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String
)