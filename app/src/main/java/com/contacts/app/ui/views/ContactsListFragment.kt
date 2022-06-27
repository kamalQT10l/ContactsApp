package com.contacts.app.ui.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.contacts.app.databinding.FragmentContactsListBinding
import com.contacts.app.helpers.ActivityActionListener
import com.contacts.app.helpers.OnContactItemClickListener
import com.contacts.app.helpers.RedirectionListener
import com.contacts.app.model.ContactModel
import com.contacts.app.ui.adapter.ContactListAdapter
import com.contacts.app.viewmodels.ContactsListViewModel


private lateinit var viewModel: ContactsListViewModel

class ContactsListFragment() : Fragment(), ActivityActionListener, OnContactItemClickListener {

    private val PERMISSION_REQUEST_CODE = 100

    private val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    private lateinit var binding : FragmentContactsListBinding
    private lateinit var redirectionListener: RedirectionListener

    private lateinit var adapter : ContactListAdapter
    private var contactList = ArrayList<ContactModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentContactsListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        redirectionListener = context as RedirectionListener
        val activityContext = context as MainActivity
        activityContext.setNavigationListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ContactsListViewModel::class.java]
        viewModel.getContactsUpdate().observe(viewLifecycleOwner, Observer {
            if(it.size == 0){
                binding.tvNoContacts.visibility = View.VISIBLE
                binding.rvContacts.visibility =View.GONE
                return@Observer
            }

            binding.rvContacts.visibility =View.VISIBLE
            contactList = it
            adapter.updateContactList(it)
            if(binding.swipeRefresLayout.isRefreshing) binding.swipeRefresLayout.isRefreshing=false
        })
        binding.rvContacts.layoutManager = linearLayoutManager
        adapter = ContactListAdapter(contactList,this)
        binding.rvContacts.adapter = adapter
        binding.swipeRefresLayout.setOnRefreshListener {
            viewModel.getContactDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        if(hasContactReadPermission()){
            viewModel.getContactDetails()
        }else{
            requestPermission()
        }
    }
    private fun hasContactReadPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getContactDetails()
            } else {
                requestPermission()
            }
        }
    }

    /**
     * Sort the contact items by first name update the recycler view
     */
    override fun onClickOfOrderByFirstName() {
        contactList.sortWith { o1, o2 ->
            val c1 = o1 as ContactModel
            val c2 = o2 as ContactModel
            c1.firstName.compareTo(c2.firstName, true)
        }

        adapter.updateContactList(contactList)
    }

    /**
     * Sort the contact items by last name update the recycler view
     */
    override fun onClickOfOrderByLastName() {
        contactList.sortWith { o1, o2 ->
            val c1 = o1 as ContactModel
            val c2 = o2 as ContactModel
            c1.lastName.compareTo(c2.lastName, true)
        }

        adapter.updateContactList(contactList)
    }

    /**
     * deletes the corresponding contact item
     */
    override fun onClickOfDelete(item: ContactModel) {
        onDeleteItemClick(item)
    }

    /**
     * redirect to details screen after clicking on contact item
     */
    override fun onItemClick(item: ContactModel) {
        redirectionListener.redirectToContactDetails(item)
    }

    /**
     * deletes the corresponding contact item
     */
    override fun onDeleteItemClick(item: ContactModel) {
        contactList.remove(item)
        adapter.updateContactList(contactList)
    }
}