package com.abdoul.smsreader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage

class MySMSReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == SMS_RECEIVED) {
            val data = intent.extras
            if (data != null) {
                //pdu -> protocol data units
                val protocolDataUnit = data["pdus"] as Array<*>?
                if (!protocolDataUnit.isNullOrEmpty()) {
                    for (element in protocolDataUnit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val format = data.getString("format")
                            val smsMessage: SmsMessage =
                                SmsMessage.createFromPdu(element as ByteArray, format)
                            val message =
                                "Sender: ${smsMessage.displayOriginatingAddress} \n" +
                                        "Body: ${smsMessage.displayMessageBody} \n" +
                                        "Time: ${smsMessage.timestampMillis} \n" +
                                        "Message: ${smsMessage.messageBody}"
                            messageListener.onMessageReceived(message)
                        } else {
                            val smsMessage: SmsMessage =
                                SmsMessage.createFromPdu(element as ByteArray)
                            val message =
                                "Sender: ${smsMessage.displayOriginatingAddress} " +
                                        "Email: ${smsMessage.emailFrom} " +
                                        "Body: ${smsMessage.displayMessageBody} " +
                                        "Time: ${smsMessage.timestampMillis} " +
                                        "Message: ${smsMessage.messageBody}"
                            messageListener.onMessageReceived(message)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        lateinit var messageListener: MessageListener

        fun bindListener(listener: MessageListener) {
            messageListener = listener
        }
    }
}
