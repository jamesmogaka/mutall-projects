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
import kotlin.system.exitProcess
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


open class MainActivity : AppCompatActivity() {

    // Initialise  variables useful although the class
    private val sendSmsCode: Int = 1
    private val retrieveSmsCode: Int = 2

    //
    //Entry point to the app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check for permissions
        checkPermission(Manifest.permission.SEND_SMS,sendSmsCode)
        checkPermission(Manifest.permission.READ_SMS,retrieveSmsCode)
        checkPermission(Manifest.permission.RECEIVE_SMS,3)

        //Initialise variables and assigning ui buttons by linking them to their ids
        val send = findViewById<Button>(R.id.send)
        val retrieve = findViewById<Button>(R.id.retrieve)
        val sendMultiple = findViewById<Button>(R.id.btnSendMultiple)
        val clear = findViewById<Button>(R.id.btnClear)

        //Set onclick listener for various functionality
        //Send sms to kplc listener
        send.setOnClickListener {
            //Get the accountInputField by its id and retrieving its text attribute
            val accountInputField: EditText = findViewById(R.id.accountInputField)
            val accountNumber = accountInputField.text.toString()
            sendSms(accountNumber)

            //Clear the accountInputField
            accountInputField.setText("")
        }
        //Read the response from inbox listener
        retrieve.setOnClickListener {
            retrieveSms()
        }
        //sendMultiple sms listener
        sendMultiple.setOnClickListener {
            // call the function that sends multiple sms
            sendMultipleSms()
        }
        //clearInbox functionality listener
        clear.setOnClickListener {
            //calling the clearInbox function
            clearInbox()
        }
    }

    //Request for the given permission ?????
    protected  fun checkPermission(permission: String,requestCode: Int){
        //Checking for permission and requesting if not granted
        //
        if (
            ContextCompat.checkSelfPermission(
                this@MainActivity,permission
                )!= PackageManager.PERMISSION_GRANTED
            ){
                ActivityCompat.requestPermissions(
                    this@MainActivity,arrayOf(permission),requestCode)

        }
    }

    //Response retrieval 
    protected fun retrieveSms() {

        //Creat the message array to store the messages
        val message = ArrayList<String>()

        //Define the columns to select
        val projection = arrayOf("address","body")
        //
        // Query the content provider through the content resolver
        val cursor = contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            "address = 97771",
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )

        // Iterat over the cursor and adding results to an array
        while (cursor?.moveToNext() == true){
            var messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString()
            message.add(messageBody)
        }

        //Creat an ArrayAdapter to display the message in the list view
        val messageArrayAdapter = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_list_item_1,
            message
            )

        //Initialise the contentBox and displaying the messages using the adopter
        val contentBox = findViewById<ListView>(R.id.contentBox)
        contentBox.adapter = messageArrayAdapter
    }

    //Sms sending 
    protected fun sendSms(accountNumber: String) {

        //Initialising the SmsManager to access the sendTextMessage method
        val manager = SmsManager.getDefault()

        //Send message if no exception is raised
        try {
            // Send the message and display an alert to inform that the message is sent
            manager.sendTextMessage(
                "97771",
                null,
                accountNumber,
                null,
                null
            )
            Toast.makeText(
                this@MainActivity,
                "Message sent",
                Toast.LENGTH_SHORT
            ).show()
        }catch (e:Exception){
            
            //investigate on exception type???????

            //Display exception message in a toast
            Toast.makeText(
                this@MainActivity,
                "$e :Please fill the account number",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    //Sends multiple sms by iteration over an array containing the message body
    protected fun sendMultipleSms(){

        //declare the array of accountNumbers <String>
        val accountNumbers = arrayOf<String>("52316642","25346821","24535786")

        //Iterate over the array and with each iteration call the sendSms function
        //Use either for or forEach to iterate over array
        //
        for(accountNumber in accountNumbers){
            //call the sendSms function with each iteration
            sendSms(accountNumber)
        }
    }

    //Delete historical records from the inbox ???????
    protected fun clearInbox(){
        contentResolver.delete(Telephony.Sms.Inbox.CONTENT_URI,null,null)
    }

    //Use the ktor library to get data from the server using the given url
    suspend fun getServerContent(url :String): String{
        //
        //Create an instance of the client
        val client = HttpClient(CIO)

        //Use the client to get a http response
        val result : HttpResponse = client.get(url)

        //Access the body of the http response
        val responseBody: String =result.body()

        //Close the client
        client.close()
        //
        //Return the body of the response as text
        return responseBody

    }

    // Check the result of the requestPermission operation
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,//????
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //
        if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            //
            //Do nothing if the permission is granted

            //Store the results in a global variable(grantResults)?????

        }else{
            //If permission is not granted show a toast and close the application
            Toast.makeText(
                this@MainActivity,
                "Permission denied",
                Toast.LENGTH_SHORT // Duration of toast
            ).show()

            //Terminate the current activity
            this.finish()
            //close the application
            exitProcess(0)
        }

    }
}