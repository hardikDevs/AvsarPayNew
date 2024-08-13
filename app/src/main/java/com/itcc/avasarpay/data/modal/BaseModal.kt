package com.itcc.avasarpay.data.modal

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BaseModal(

	@field:SerializedName("message")
	 val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean?=null,

	@SerializedName("item")
	val customer: Error?,
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
		parcel.readParcelable(Error::class.java.classLoader)
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(message)
		parcel.writeValue(status)
		parcel.writeParcelable(customer, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<BaseModal> {
		override fun createFromParcel(parcel: Parcel): BaseModal {
			return BaseModal(parcel)
		}

		override fun newArray(size: Int): Array<BaseModal?> {
			return arrayOfNulls(size)
		}
	}
}

data class Error(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString()
	) {
	}

	override fun describeContents(): Int {
		return 0
	}

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeString(msg)
		dest.writeValue(code)
		dest.writeString(title)
	}

	companion object CREATOR : Parcelable.Creator<Error> {
		override fun createFromParcel(parcel: Parcel): Error {
			return Error(parcel)
		}

		override fun newArray(size: Int): Array<Error?> {
			return arrayOfNulls(size)
		}
	}
}
