package com.itcc.avasarpay.data.modal

import com.google.gson.annotations.SerializedName

data class CategoryListModal(

    @field:SerializedName("item")
    val item: List<CategoryItem> = mutableListOf(),

    ) : BaseModal()

data class CategoryItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("featured_image")
    val featuredImage: String? = null
)
