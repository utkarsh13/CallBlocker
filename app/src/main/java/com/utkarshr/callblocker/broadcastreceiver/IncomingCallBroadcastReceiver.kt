package com.utkarshr.callblocker.broadcastreceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.utkarshr.callblocker.database.SpamNumberDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class IncomingCallBroadcastReceiver : BroadcastReceiver() {
    private val LOG_TAG: String = IncomingCallBroadcastReceiver::class.java.simpleName

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
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
            Log.i(LOG_TAG, String.format("Incoming call from %s", phoneNumber))

            val dao = SpamNumberDatabase.getInstance(context).dao
            Observable.fromCallable { dao.getAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null) {
                        val matches = it.map { it ->
                            it.regex
                        }
                            .filter { it -> phoneNumber.matches(it) }

                        Log.i(LOG_TAG, "Matches $matches")
                        if(matches.count() > 0) {
                            val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                            telecomManager.endCall()
                        }
                    }
                }, { error ->
                    Log.i(LOG_TAG, "Not able to fetch data\n$error")
                })
        }

    }

}