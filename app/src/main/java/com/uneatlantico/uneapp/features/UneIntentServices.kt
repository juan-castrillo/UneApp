package com.uneatlantico.uneapp.features

import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent
import android.app.IntentService
import android.app.Notification
import com.uneatlantico.uneapp.Inicio.InicioActivity
import com.uneatlantico.uneapp.R


/*class UneIntentServices : IntentService("MyNewIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val builder = Notification.Builder(this)
        builder.setContentTitle("My Title")
        builder.setContentText("This is the Body")
        builder.setSmallIcon(R.drawable.settings)
        val notifyIntent = Intent(this, InicioActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }

    companion object {
        private val NOTIFICATION_ID = 3
    }
}*/