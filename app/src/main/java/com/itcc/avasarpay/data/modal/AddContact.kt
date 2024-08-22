package com.itcc.avasarpay.data.modal

import java.io.Serializable

data class AddContactReq(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
   )

data class AddContactRes(
    var item: ArrayList<ContactData>
) : BaseModal()



data class ContactData(
    var id:  String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var created_at: String? = null,
): Serializable {
    override fun toString(): String {
        return name.toString()
    }
}
