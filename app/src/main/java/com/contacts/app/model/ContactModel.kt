package com.contacts.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactModel(val firstName: String, val lastName: String, val email:String?, val number:String?): Parcelable