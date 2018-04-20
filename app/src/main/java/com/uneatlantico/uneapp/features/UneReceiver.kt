package com.uneatlantico.uneapp.features

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class UneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val intent1 = Intent(context, UneIntentServices::class.java)
        context.startService(intent1)
    }
}