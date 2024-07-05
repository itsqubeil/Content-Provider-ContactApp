package test.dapuk.contactaoo

import android.app.Activity
import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import test.dapuk.contactaoo.databinding.ActivityAddContactBinding

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2)

        binding.buttonSaveContact.setOnClickListener {
            addContact()
        }
    }

    private fun addContact() {
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhone.text.toString()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and Phone cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val ops = ArrayList<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK) // Set result OK
            finish() // Close the activity and return to the previous one
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show()
        }
    }
}