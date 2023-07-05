package com.example.myapplication.ui.Tab1


import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.Fragment1Binding

class Fragment1 : Fragment() {

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactAdapter

    private lateinit var contacts: MutableList<Contact>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val fragment1ViewModel = ViewModelProvider(this).get(Fragment1ViewModel::class.java)

        val recyclerView = binding.recyclerViewContacts
        recyclerView.layoutManager = LinearLayoutManager(context)



        // Permission 체크를 수행합니다.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            // Permission이 허용된 경우 연락처를 가져옵니다.
            contacts = getContacts().toMutableList()
            adapter = ContactAdapter(contacts)
            recyclerView.adapter = adapter
        }



        val addButton = binding.addButton
        addButton.setOnClickListener {
            navigateToAddContactFragment()
        }

        return root
    }

    private fun navigateToAddContactFragment() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_fragment1_to_addContactFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra("name")
            val phoneNumber = data?.getStringExtra("phoneNumber")
            if (!name.isNullOrEmpty() && !phoneNumber.isNullOrEmpty()) {
                val contact = Contact(name, phoneNumber)
                contacts.add(contact)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            val nameColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
            val phoneNumberColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = if (nameColumnIndex >= 0) it.getString(nameColumnIndex) else ""
                val phoneNumber =
                    if (phoneNumberColumnIndex >= 0) it.getString(phoneNumberColumnIndex) else ""
                val contact = Contact(name, phoneNumber)
                contacts.add(contact)
            }
        }
        return contacts
    }

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val CONTACT_REQUEST_CODE = 101
    }
}
