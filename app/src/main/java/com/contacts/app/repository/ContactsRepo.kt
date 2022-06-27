package com.contacts.app.repository

import android.app.Application
import com.contacts.app.model.ContactModel
import java.util.ArrayList

interface ContactsRepo {
   suspend fun getContacts(application: Application) : ArrayList<ContactModel>
}