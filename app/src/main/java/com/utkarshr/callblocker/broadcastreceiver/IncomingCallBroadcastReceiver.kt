package com.utkarshr.callblocker.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log


class IncomingCallBroadcastReceiver : BroadcastReceiver() {
    private val LOG_TAG: String = IncomingCallBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED != intent?.action) {
            Log.e(LOG_TAG, String.format("IncomingCallReceiver called with incorrect intent action: %s",intent?.action))
            return
        }

        val newState: String? = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        Log.d(LOG_TAG, String.format("Call state changed to %s", newState))

        if (TelephonyManager.EXTRA_STATE_RINGING == newState) {
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (phoneNumber == null) {
                Log.d(LOG_TAG, "Ignoring empty phone number broadcast receiver");
                return
            }
            Log.i(LOG_TAG, String.format("Incoming call from %s", phoneNumber));

            val regex = Regex("555\\d*")

            Log.i(LOG_TAG, "Regex value " + phoneNumber.matches(regex));
            if (phoneNumber.matches(regex)) {
                val telecomManager = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                telecomManager.endCall()
                Log.i(LOG_TAG, String.format("Call ended for phone number %s", phoneNumber));
            }
        }

    }

}