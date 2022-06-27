package com.contacts.app.repository

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.text.TextUtils
import android.util.Patterns
import com.contacts.app.model.ContactModel
import java.util.*


class ContactsRepoImpl : ContactsRepo {

    /**
     * fetches contact data from android contact app
     */
    override suspend fun getContacts(application: Application): ArrayList<ContactModel> {
        val arrayList = ArrayList<ContactModel>()
        val map = HashMap<String, ContactModel>()
        val contentResolver: ContentResolver = application.contentResolver


        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1' ",
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIMIT 10"
        )

        if ((cursor != null) && cursor.moveToFirst()) {
            do {
                // get the contact's information
                val contactIdKey = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val id = cursor
                    .getString(contactIdKey)
                val contactDisplayNameKey = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val name :String= cursor.getString(contactDisplayNameKey)


                val rawId=cursor.getColumnIndex(ContactsContract.RawContacts._ID)
                val rawContactId: Long = cursor.getLong(rawId)

                var lastName = ""
                var firstName: String? = ""
                val contactUri: Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rawContactId)
                val dataUri: Uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Data.CONTENT_DIRECTORY)
                val nameCursor = application.contentResolver.query(
                    dataUri,
                    null,
                    ContactsContract.Contacts.Data.MIMETYPE.toString() + "=?",
                    arrayOf(StructuredName.CONTENT_ITEM_TYPE),
                    null
                )
                if (nameCursor != null) {
                    while (nameCursor.moveToNext()) {
                        val fs =nameCursor.getColumnIndex(ContactsContract.Contacts.Data.DATA2)
                        if(nameCursor.getString(fs)!=null){
                             firstName =
                                nameCursor.getString(fs)
                        }
                        val ls=nameCursor.getColumnIndex(ContactsContract.Contacts.Data.DATA3)
                        if(nameCursor.getString(ls)!=null){
                             lastName = nameCursor.getString(ls)
                        }
                    }
                }
                nameCursor?.close()

                val hasPhoneKey = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                val hasPhone = cursor
                    .getInt(hasPhoneKey)


                // get the user's email address
                var email: String? = null
                val cursorEmail = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id),
                    null
                )
                if (cursorEmail != null && cursorEmail.moveToFirst()) {
                    val emailKey = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                    email =
                        cursorEmail.getString(emailKey)
                    cursorEmail.close()
                }

                // get the user's phone number
                var phone: String? = null
                if (hasPhone > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (cursorPhone != null && cursorPhone.moveToFirst()) {
                        val numberKey = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        phone =
                            cursorPhone.getString(numberKey)
                        cursorPhone.close()
                    }
                }

                // if the user has an email or phone then add it to contacts
                if ((!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            && !Objects.requireNonNull(email)
                        .equals(name, ignoreCase = true)) || !TextUtils.isEmpty(phone)
                ) {
                    if(firstName!=null){
                        val contactModel = ContactModel(firstName,lastName,email,phone)
                        if (!map.containsKey(phone)) map[phone!!] = contactModel
                    }
                }
            } while (cursor.moveToNext())
            cursor.close()

            //sorting the contacts
            for (mapData in map.entries){
                arrayList.add(mapData.value)
            }

            arrayList.sortWith(Comparator { o1, o2 ->
                val c1 = o1 as ContactModel
                val c2 = o2 as ContactModel
                c1.firstName.compareTo(c2.firstName, true)
            })
        }
        return arrayList
    }


}