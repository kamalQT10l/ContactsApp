package com.contacts.app.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.contacts.app.model.ContactModel
import com.contacts.app.repository.ContactsRepo
import com.contacts.app.repository.ContactsRepoImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactsListViewModel(application: Application) : AndroidViewModel(application) {
    private var contactsRepo: ContactsRepo = ContactsRepoImpl()

    private var _contactMutableLiveData : MutableLiveData<ArrayList<ContactModel>> = MutableLiveData()

    private var liveData : LiveData<ArrayList<ContactModel>> = _contactMutableLiveData

    /**
     * get contact details from repository
     */
    fun getContactDetails(){
        viewModelScope.launch {
            val contactListAsync = async { contactsRepo.getContacts(getApplication()) }
            val contactList = contactListAsync.await()
            _contactMutableLiveData.postValue(contactList)
        }
    }

    fun getContactsUpdate() : LiveData<ArrayList<ContactModel>>{
        return liveData
    }
}