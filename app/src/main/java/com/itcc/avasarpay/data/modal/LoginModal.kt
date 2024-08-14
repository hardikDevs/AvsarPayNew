package com.itcc.avasarpay.data.modal


import com.google.gson.annotations.SerializedName


data class LoginModal(
    var token: String? = null,
    var item: User
) : BaseModal()


data class User(
    var id: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var about: String? = null,
    var wallet: String? = null,
    var created_at: String? = null,
)