package com.contacts.app.helpers

import com.contacts.app.model.ContactModel

interface OnContactDetailsListener {
    fun onDeleteItem(item : ContactModel)
}