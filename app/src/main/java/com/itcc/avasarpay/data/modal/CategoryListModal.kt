package com.itcc.avasarpay.data.modal

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryListModal(

	@field:SerializedName("item")
	val item: List<CategoryItem> = mutableListOf(),

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class TemplatesItem(

	@field:SerializedName("preview_link")
	val previewLink: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("id")
	val id: Int = 0,

	var isSelected: Boolean = false

):Serializable

data class CategoryItem(

	@field:SerializedName("templates")
	val templates: List<TemplatesItem> = mutableListOf(),

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("featured_image")
	val featuredImage: String? = null,

	var isSelected: Boolean = false
):Serializable


