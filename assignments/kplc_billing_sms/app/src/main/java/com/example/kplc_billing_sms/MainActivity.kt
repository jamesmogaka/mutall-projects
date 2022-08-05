package com.example.kplc_billing_sms

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Telephony
import android.widget.*


open class MainActivity : AppCompatActivity() {

    // Initialising  variables useful although the class
    private val sendSmsCode: Int = 1
    private val retrieveSmsCode: Int = 2

    //Getting the accountInputField by its id and retrieving its text attribute
    private val accountInputField: EditText = findViewById(R.id.accountInputField)
    private val accountNumber = accountInputField.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialising variables and assigning ui buttons by linking them to their ids
        val send = findViewById<Button>(R.id.send)
        val retrieve = findViewById<Button>(R.id.retrieve)

        //Setting onclick listener for various functionality
        //Sending sms to kplc
        send.setOnClickListener {
            checkPermission(Manifest.permission.SEND_SMS,sendSmsCode)
        }

        //Reading the response from inbox
        retrieve.setOnClickListener {
            checkPermission(Manifest.permission.READ_SMS,retrieveSmsCode)
        }
    }
    protected  fun checkPermission(permission: String,requestCode: Int){
        //Checking for permission and requesting if not granted
        //
        if (ContextCompat.checkSelfPermission(this@MainActivity,permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)

        }else {
            //Checking which permission is requested to invoke relevant function
            when(permission){
                Manifest.permission.SEND_SMS -> sendSms(accountNumber)
                Manifest.permission.READ_SMS -> retrieveSms()
            }
        }
    }

    //Response retrieval functionality
    protected fun retrieveSms() {
        //Creating the message array to store the messages
        val message = ArrayList<String>()

        // Querying the content provider through the content resolver
        val projection = arrayOf("address","body")
        val cursor = contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI,projection,"address = 97771",null,Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

        // Iterating over the cursor and adding results to an array defined above(message)
        while (cursor?.moveToNext() == true){
            var messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
            message.add(messageBody)
        }

        //Creating an ArrayAdapter to display the message in the list view
        val messageArrayAdapter = ArrayAdapter<String>(this@MainActivity,android.R.layout.simple_list_item_1,message)

        //Initialising the contentBox and displaying the messages using the adopter
        val contentBox = findViewById<ListView>(R.id.contentBox)
        contentBox.adapter = messageArrayAdapter
    }

    //Sms sending functionality
    protected fun sendSms(accountNumber: String) {

        //Initialising the SmsManager to access the sendTextMessage method
        val manager = SmsManager.getDefault()
        //
        //Send message if no exception is raised
        try {
            // Send the message and display an alert to inform that the message is sent
            manager.sendTextMessage("97771",null,accountNumber,null,null)
            //Clear the accountInputField
            accountInputField.setText("")
            Toast.makeText(this@MainActivity, "Message sent", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            //Display exception message in a toast
            Toast.makeText(this@MainActivity, "Please fill the account number", Toast.LENGTH_SHORT).show()
        }
    }
    //Sends multiple sms by iteration over an array containing the message body
    protected fun sendMultipleSms(){
        //declare the array of messages <String>
        val accountNumbers = ArrayList<String>()
        //Iterate over the array and with each iteration call the sendSms function
        for(accountNumber in accountNumbers){
            sendSms(accountNumber)
        }
    }

    // Check the result of the requestPermission operation above
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            //Checking the request code to invoke relevant functionality
            when(requestCode){
                sendSmsCode -> {
                    Toast.makeText(this@MainActivity, "Sms Permission granted", Toast.LENGTH_SHORT).show()
                    sendSms(accountNumber)
                }
                retrieveSmsCode -> {
                    Toast.makeText(this@MainActivity, "Inbox permission Granted", Toast.LENGTH_SHORT).show()
                    retrieveSms()
                }
            }
        }else{
            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
        }

    }
}