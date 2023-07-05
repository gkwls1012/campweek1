package com.example.myapplication.ui.Tab1

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddContactBinding
import android.Manifest

class AddContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val view = binding.root

        val doneButton = binding.doneButton
        doneButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val contact = Contact(name, phoneNumber)

                // 권한이 허용되었는지 확인
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한이 이미 허용된 경우 연락처 추가 진행
                    addContact(name, phoneNumber)
                    navigateToFragment1(contact)
                } else {
                    // 권한이 허용되지 않은 경우 권한 요청
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.WRITE_CONTACTS),
                        PERMISSION_REQUEST_CODE
                    )
                }

            } else {
                Toast.makeText(requireContext(), "Please enter name and phone number", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 연락처 추가 진행
                val name = binding.nameEditText.text.toString()
                val phoneNumber = binding.phoneNumberEditText.text.toString()
                val contact = Contact(name, phoneNumber)

                addContact(name, phoneNumber)
                navigateToFragment1(contact)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Write Contacts permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addContact(name: String, phoneNumber: String){
        val contentResolver: ContentResolver = requireActivity().contentResolver

        val rawContactValues = ContentValues().apply {
            putNull(ContactsContract.RawContacts.ACCOUNT_TYPE)
            putNull(ContactsContract.RawContacts.ACCOUNT_NAME)
        }

        val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, rawContactValues)
        val rawContactId = rawContactUri?.lastPathSegment?.toLongOrNull()

        val nameValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        val phoneValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        }

        contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameValues)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)

    }

    private fun navigateToFragment1(contact: Contact) {
        val resultIntent = Intent()
        resultIntent.putExtra("name", contact.name)
        resultIntent.putExtra("phoneNumber", contact.phoneNumber)
        requireActivity().setResult(Activity.RESULT_OK, resultIntent)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.addContactFragment_to_action_fragment1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_REQUEST_CODE = "requestCode"

        fun newInstance(requestCode: Int): AddContactFragment {
            val fragment = AddContactFragment()
            val args = Bundle()
            args.putInt(ARG_REQUEST_CODE, requestCode)
            fragment.arguments = args
            return fragment
        }
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
