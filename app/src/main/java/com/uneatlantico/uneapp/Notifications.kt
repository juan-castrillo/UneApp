package com.uneatlantico.uneapp

import android.app.Activity
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uneatlantico.uneapp.Inicio.InicioActivity


class Notifications: FirebaseMessagingService() {



    private val REQUEST_CODE = 1
    private val NOTIFICATION_ID = 6578



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("inicio", "idk")
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("body")
        Log.d("mensaje", message)
        Log.d("titulo", title)
        showNotifications(title!!, message!!)
    }

    private fun showNotifications(title: String, msg: String) {
        /*val i = Intent(this, InicioActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT)*/

        val notification = NotificationCompat.Builder(this)
                .setContentText(msg)
                .setContentTitle(title)
                //.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}