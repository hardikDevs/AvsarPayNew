package com.itcc.avasarpay.data.modal

import java.io.Serializable


data class CateListRes(
    var item: ArrayList<CateListData>
) : BaseModal()



data class CateListData(
    var id: String? = null,
    var name: String? = null,
    var featured_image: String? = null,
    var created_at: String? = null,
    var templates: ArrayList<TemplatesListData> = ArrayList()
): Serializable {
    override fun toString(): String {
        return name.toString()
    }
}

data class TemplatesListData(
    var id: String? = null,
    var category_id: String? = null,
    var name: String? = null,
    var photo: String? = null,
): Serializable