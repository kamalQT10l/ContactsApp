package com.contacts.app.ui.views

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.contacts.app.R
import com.contacts.app.databinding.FragmentContactDetailsBinding
import com.contacts.app.helpers.OnContactDetailsListener
import com.contacts.app.model.ContactModel
import com.contacts.app.utils.AppConstants

/**
 * A simple [Fragment] subclass.
 * Use the [ContactDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactDetailsFragment(private var onContactDetailsListener: OnContactDetailsListener) : Fragment() {

    private lateinit var contactItem : ContactModel
    private lateinit var binding : FragmentContactDetailsBinding
    private lateinit var activityReference : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            contactItem = bundle.getParcelable<Parcelable>(AppConstants.CONTACT_ITEM) as ContactModel
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onContactDetailsListener = context as OnContactDetailsListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupListeners()
    }

    private fun setupListeners() {
        binding.ivDelete.setOnClickListener {
            onContactDetailsListener.onDeleteItem(contactItem)
            Toast.makeText(requireActivity(),activityReference.resources.getString(R.string.delete_msg), Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupUi() {
        activityReference = (this.activity as MainActivity).activityContext
        binding.tvFirstName.text = contactItem.firstName
        if(contactItem.lastName.isNotEmpty()){
            binding.tvLastName.text = contactItem.lastName
        }else{
            binding.tvLastName.text = resources.getString(R.string.str_not_available)
        }
        binding.tvMobileNo.text = contactItem.number
        if(contactItem.email?.isNotEmpty() == true){
            binding.tvEmailId.text = contactItem.email
        }else{
            binding.tvFnLabel.text = activityReference.resources.getString(R.string.str_not_available)
        }

        binding.tvFnLabel.text = activityReference.resources.getString(R.string.str_first_name)
        binding.tvLastNameLabel.text = activityReference.resources.getString(R.string.str_last_name)
        binding.tvMobileNoLabel.text = activityReference.resources.getString(R.string.str_mobile_no)
        binding.tvEmailLabel.text = activityReference.resources.getString(R.string.str_email_id)
    }
}