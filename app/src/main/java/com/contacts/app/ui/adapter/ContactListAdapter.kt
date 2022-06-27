package com.contacts.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.contacts.app.databinding.ItemContactLayoutBinding
import com.contacts.app.helpers.ContactDiffCallBack
import com.contacts.app.helpers.OnContactItemClickListener
import com.contacts.app.model.ContactModel

class ContactListAdapter(private val contactModel: ArrayList<ContactModel>,
private val onContactItemClickListener : OnContactItemClickListener
) :  RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    /**
     * Compares the two list and update the recycler view adapter with the minimum required changes
     */
    fun updateContactList(contactModels: ArrayList<ContactModel>) {
        val diffCallback = ContactDiffCallBack(this.contactModel, contactModels)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.contactModel.clear()
        this.contactModel.addAll(contactModels)
        diffResult.dispatchUpdatesTo(this)
    }


   inner class ContactViewHolder(private val binding: ItemContactLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
       init {
           binding.clItemRoot.setOnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   val item = contactModel[position]
                   onContactItemClickListener.onItemClick(item)
               }
           }
           binding.ivDeleteItem.setOnClickListener {
               val position = adapterPosition
               if (position != RecyclerView.NO_POSITION) {
                   val item = contactModel[position]
                   onContactItemClickListener.onDeleteItemClick(item)
                   binding.swLl.close(false)
               }
           }

       }


       fun bind(item: ContactModel?) {
           item?.let {
               binding.tvName.text =   it.firstName.plus(it.lastName)
               binding.tvNumber.text=it.number
           }
       }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactModel[position])
    }

    override fun getItemCount(): Int {
       return contactModel.size
    }

}