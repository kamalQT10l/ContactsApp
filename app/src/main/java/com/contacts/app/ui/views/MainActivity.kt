package com.contacts.app.ui.views

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.contacts.app.R
import com.contacts.app.databinding.ActivityMainBinding
import com.contacts.app.helpers.ActivityActionListener
import com.contacts.app.helpers.OnContactDetailsListener
import com.contacts.app.helpers.RedirectionListener
import com.contacts.app.model.ContactModel
import com.contacts.app.utils.AppConstants
import com.contacts.app.utils.AppConstants.Companion.CONTACT_ITEM
import com.contacts.app.utils.LocaleHelper


class MainActivity : AppCompatActivity(), RedirectionListener, OnContactDetailsListener {
    private lateinit var binding : ActivityMainBinding
    lateinit var activityContext : Context
    private lateinit var navigationMenuClickListener : ActivityActionListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setUserActions()
    }



    private fun initViews() {
        setSupportActionBar(binding.includeLayoutMain.includeLayoutToolbar.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.includeLayoutMain.includeLayoutToolbar.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.includeLayoutNav.rdGroupSortBy.visibility=View.GONE
        binding.includeLayoutNav.rdGroupLanguage.visibility=View.GONE

        supportFragmentManager.beginTransaction().add(R.id.fragment_container_contacts,ContactsListFragment()).commit()
    }

    private fun setUserActions() {
        binding.includeLayoutNav.tvSortBy.setOnClickListener {
            if(binding.includeLayoutNav.rdGroupSortBy.visibility == View.VISIBLE){
                binding.includeLayoutNav.rdGroupSortBy.visibility=View.GONE
            }else{
                binding.includeLayoutNav.rdGroupSortBy.visibility=View.VISIBLE
            }
        }

        binding.includeLayoutNav.tvLanguage.setOnClickListener {
            if(binding.includeLayoutNav.rdGroupLanguage.visibility == View.VISIBLE){
                binding.includeLayoutNav.rdGroupLanguage.visibility=View.GONE
            }else{
                binding.includeLayoutNav.rdGroupLanguage.visibility=View.VISIBLE
            }
        }

        binding.includeLayoutNav.rdGroupSortBy.setOnCheckedChangeListener { _, i ->
            if(binding.includeLayoutNav.radioFirstName.id == i){
                navigationMenuClickListener.onClickOfOrderByFirstName()
            }else if(binding.includeLayoutNav.radioLastName.id == i){
                navigationMenuClickListener.onClickOfOrderByLastName()
            }
        }

        binding.includeLayoutNav.rdGroupLanguage.setOnCheckedChangeListener { _, i ->
            if(binding.includeLayoutNav.radioEnglish.id == i){
              activityContext = LocaleHelper.setLocale(this@MainActivity, AppConstants.LANGUAGE_ENGLISH)
            }else if(binding.includeLayoutNav.radioHindi.id == i){
                activityContext =  LocaleHelper.setLocale(this@MainActivity, AppConstants.LANGUAGE_HINDI)
            }
            updateUi()
        }
    }

    private fun updateUi() {
        binding.includeLayoutNav.radioFirstName.text = activityContext.resources.getString(R.string.str_first_name)
        binding.includeLayoutNav.radioLastName.text = activityContext.resources.getString(R.string.str_last_name)
        binding.includeLayoutNav.tvLanguage.text = activityContext.resources.getString(R.string.str_language)
        binding.includeLayoutNav.tvSortBy.text = activityContext.resources.getString(R.string.str_sort_by)
    }

    fun setNavigationListener(navigationMenuClickListener: ActivityActionListener){
        this.navigationMenuClickListener=navigationMenuClickListener
    }

    /**
     * navigates to the corresponding Contact details screen once the item clicked
     */
    override fun redirectToContactDetails(item: ContactModel) {
        val bundle = Bundle()
        bundle.putParcelable(CONTACT_ITEM,item)
        val fragmentInstance = ContactDetailsFragment(this)
        fragmentInstance.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_contacts,fragmentInstance).
            addToBackStack(ContactDetailsFragment::class.java.simpleName).commit()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        activityContext= newBase
    }


    /**
     * deletes the contact item & redirect to contact list screen
     */
    override fun onDeleteItem(item: ContactModel) {
        supportFragmentManager.popBackStack()
        navigationMenuClickListener.onClickOfDelete(item)
    }
}