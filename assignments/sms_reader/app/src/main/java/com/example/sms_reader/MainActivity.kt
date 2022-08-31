package com.example.sms_reader

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialising and assignment of the button
        val retrieve = findViewById<Button>(R.id.retrieve)

        //initial checking to see if the permissions are granted
        //
        retrieve.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.READ_SMS
                )!= PackageManager.PERMISSION_GRANTED){
                //requesting of permissions from the user
                //
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_SMS),
                    1
                )
            }else{
                readSms()
            }
        }
    }

    private fun readSms() {
        //initialising & assigning my ui elements
        val addressBox = findViewById<TextView>(R.id.addressBox)
        val contentBox = findViewById<ListView>(R.id.contentBox)

        //array that stores the messages fetched from the database
        var messages = ArrayList<String>()


        //getting input from address box
        var phoneNo = addressBox.text.toString()

        //content providers make it easy and secure for applications to exchange data(Telephony.Sms.inbox)
        //create an instance of a content resolver class (it is already a predefined variable)
        //a content resolver is useful in querying a content provider(Telephony.Sms.inbox)
        val projection = arrayOf(
            Telephony.TextBasedSmsColumns.ADDRESS,
            Telephony.TextBasedSmsColumns.BODY
        ) //columns i need
        val whereClause: String ="address = ?" // condition to be satisfied
        val cursor = contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI,projection,whereClause,
            arrayOf(phoneNo),Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)!!
        //results from a content resolver query are in a form of a cursor object
        //to iterate over the cursor we use moveToNext()<Boolean> method

        while (cursor.moveToNext()){
            //using the cursor to retrieve the column index then retrieve the records using getString method
            var addressContent =cursor.getString(cursor.getColumnIndexOrThrow("address")).toString()
            var messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
            //compile the results in a string and add to my array of messages
            messages.add("Number: $addressContent -> $messageBody ")
        }

        //displaying the content of the messages array to our Textview
        //creating an array adaptor view

        val messageAdaptorView = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_list_item_1,
            messages
        )
        // binding the adapter view to its layout
        //
        contentBox.adapter = messageAdaptorView
    }

    //checking weather the user granted permission after permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //actual checking of the grantResult array which stores the results of the request permission from the user
        //
        if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readSms()
            Toast.makeText(this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}