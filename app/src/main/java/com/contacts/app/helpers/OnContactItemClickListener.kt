package com.contacts.app.helpers

import com.contacts.app.model.ContactModel

interface OnContactItemClickListener {
    fun onItemClick(item : ContactModel)

    fun onDeleteItemClick(item: ContactModel)
}