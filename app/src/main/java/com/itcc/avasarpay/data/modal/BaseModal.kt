package com.itcc.avasarpay.data.modal

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

open class BaseModal {

	var message : String? = null
	var status : Boolean = false

	override fun toString(): String {
		return Gson().toJson(this)
	}
}
