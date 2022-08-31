package com.example.sms_app

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.telephony.SmsManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {
            //check the permission
            //if granted continue to sent message
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED){
                sendMessage()
            }else{
                // requesting the permission from the user


                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    1
                )
            }

        }
    }

    //
    // function that actually sends the message
    private fun sendMessage(){
        //initialising my ui elements
        // linking ui elements using their ids
        //
        var addressBox: EditText = findViewById(R.id.addressBox)
        var messageBox: EditText = findViewById(R.id.messageBox)

        //Getting input from ui elements
        val phoneNo: String = addressBox.text.toString()
        val message: String = messageBox.text.toString()
        //initialisation of the SmsManager
        val manager = SmsManager.getDefault()
        //sending message
        //
        manager.sendTextMessage(phoneNo,null,message,null,null)

        Toast.makeText(this@MainActivity, "Message sent", Toast.LENGTH_SHORT).show()
    }
    // examines if the permission is granted or denied after requesting the permission from the user
    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendMessage()
            Toast.makeText(this@MainActivity, "Message sent", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(
                this@MainActivity,
                "Permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
   }
}