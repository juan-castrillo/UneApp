package com.uneatlantico.uneapp.Inicio.navbar_frags

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uneatlantico.uneapp.R



/**
 * Fragmento del Horario
 */
class HorarioFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //notificaciones()
        return inflater.inflate(R.layout.activity_horario, container, false)
    }


    /*fun notificaciones(){
        val notifyIntent = Intent(this.context, UneReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                (1000 * 60 * 60 * 24).toLong(), pendingIntent)
    }*/

    companion object {
        fun newInstance(): HorarioFragment = HorarioFragment()
    }
}// Required empty public constructor
