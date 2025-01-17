package com.itcc.avasarpay.data.modal

/**
 *Created By Sunny on 13-12-2024.
 */
data class Contact(
    val id: Int,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList(),
    var isSelected: Boolean = false
)
