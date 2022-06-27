package com.contacts.app.helpers

import androidx.recyclerview.widget.DiffUtil
import com.contacts.app.model.ContactModel

/***
 * Class that compares the two list and returns the minimum no of changes that required in the list
 */
class ContactDiffCallBack(oldContactList: List<ContactModel>, newContactList: List<ContactModel>) :
    DiffUtil.Callback() {
    private val mOldContactModelList: List<ContactModel>
    private val mNewContactModelList: List<ContactModel>
    override fun getOldListSize(): Int {
        return mOldContactModelList.size
    }

    override fun getNewListSize(): Int {
        return mNewContactModelList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldContactModelList[oldItemPosition].number === mNewContactModelList[newItemPosition].number
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: ContactModel = mOldContactModelList[oldItemPosition]
        val newContact: ContactModel = mNewContactModelList[newItemPosition]
        return oldContact.number.equals(newContact.number)
    }


    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

    init {
        mOldContactModelList = oldContactList
        mNewContactModelList = newContactList
    }
}