package com.contacts.app.helpers

import com.contacts.app.model.ContactModel

interface RedirectionListener {
    fun redirectToContactDetails(item : ContactModel)
}