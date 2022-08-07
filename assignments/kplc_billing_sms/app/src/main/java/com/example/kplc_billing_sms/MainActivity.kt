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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check for permissions
        checkPermission(Manifest.permission.SEND_SMS,sendSmsCode)
        checkPermission(Manifest.permission.READ_SMS,retrieveSmsCode)

        //Initialising variables and assigning ui buttons by linking them to their ids
        val send = findViewById<Button>(R.id.send)
        val retrieve = findViewById<Button>(R.id.retrieve)

        //Setting onclick listener for various functionality
        //Sending sms to kplc
        send.setOnClickListener {
            //Getting the accountInputField by its id and retrieving its text attribute
            val accountInputField: EditText = findViewById(R.id.accountInputField)
            val accountNumber = accountInputField.text.toString()
            sendSms(accountNumber)

            //Clear the accountInputField
            accountInputField.setText("")
        }

        //Reading the response from inbox
        retrieve.setOnClickListener {
            retrieveSms()
        }
    }
    protected  fun checkPermission(permission: String,requestCode: Int){
        //Checking for permission and requesting if not granted
        //
        if (
            ContextCompat.checkSelfPermission(this@MainActivity,permission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)

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
            Toast.makeText(this@MainActivity, "Message sent", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){

            //Display exception message in a toast
            Toast.makeText(this@MainActivity, "$e :Please fill the account number", Toast.LENGTH_SHORT).show()
        }
    }
    //Sends multiple sms by iteration over an array containing the message body
    protected fun sendMultipleSms(){
        //declare the array of accountNumbers <String>
        val accountNumbers = ArrayList<String>()

        //Iterate over the array and with each iteration call the sendSms function
        //Use either for or forEach to iterate over array
        //
        for(accountNumber in accountNumbers){
            //call the sendSms function with each iteration
            sendSms(accountNumber)
        }
    }

    // functionality to delete historical records from the inbox
    protected fun clearInbox(){
        contentResolver.delete(Telephony.Sms.Inbox.CONTENT_URI,"address = 97771",null)
    }

    // Check the result of the requestPermission operation above
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            //Do nothing if the permission is granted

        }else{
            //If permission is not granted show a toast and close the application
            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            //close the application
        }

    }
}