package com.itcc.avasarpay.data.modal

data class RegisterReq(
    var name: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var about: String? = null,
    var type: String? = null,
    var uid: String? = null,
    var social_id: String? = null,
    var platform: String? = null,
    var os_version: String? = null,
    var application_version: String? = null,
    var model: String? = null,)

data class RegisterRes(
    var token: String? = null,
    var item: User
) : BaseModal()

