package com.example.kplc_billing_sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log


class SmsReceiver:BroadcastReceiver() {
    companion object{
        private val TAG by lazy { SmsReceiver:: class.java.simpleName }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        ///Check if the broadcast from the android system is about an sms received
        //If not exit the function
        if ( !intent?.action.equals( Telephony.Sms.Intents.SMS_RECEIVED_ACTION ) ) return

        //Extract the message from the intent passed by the android system
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        //????? specific address messages

        //Iterate over the messages and log them to the console
        extractMessages.forEach {smsMessage -> Log.v( TAG, smsMessage.displayMessageBody ) }
    }
}