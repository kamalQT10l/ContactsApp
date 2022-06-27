package com.contacts.app.helpers

import com.contacts.app.model.ContactModel

interface ActivityActionListener {

    fun onClickOfOrderByFirstName()

    fun onClickOfOrderByLastName()

    fun onClickOfDelete(item:ContactModel)
}