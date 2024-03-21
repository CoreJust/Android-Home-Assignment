package ilyin.yegor.homeassignment1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val PICK_CONTACT = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verifyPermissions()

        val edit_text = findViewById<EditText>(R.id.editTextTextMultiLine)
        val get_contact_btn = findViewById<Button>(R.id.button_get_contact)
        val send_data_btn = findViewById<Button>(R.id.button_send_data)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data;
                val contact = data!!.data!!;
                val cursor = contentResolver.query(contact, null, null, null, null)!!

                if (cursor.moveToFirst()) {
                    val contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                    val hasPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt();
                    var contactNumber = "unknown"

                    if (hasPhoneNumber == 1) {
                        val cursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null
                        )

                        if (cursor!!.moveToFirst()) {
                            contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }

                        cursor.close()
                    }

                    edit_text.setText("$contactName (id $contactId) : $contactNumber")
                }

                cursor.close()
            }
        }

        get_contact_btn.setOnClickListener {
            val contactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            launcher.launch(contactIntent)
        }

        send_data_btn.setOnClickListener {
            val intent = Intent(this@MainActivity, DataActivity::class.java)
            intent.putExtra("Contact", edit_text.text.toString())

            startActivity(intent)
        }
    }

    private fun verifyPermissions() {
        val CONTACT_PERMISSION_CODE = 1;
        val permission = android.Manifest.permission.READ_CONTACTS;
        while (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), CONTACT_PERMISSION_CODE)
        }
    }
}
