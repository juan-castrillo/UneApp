package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uneatlantico.uneapp.R
import android.support.v4.view.ViewPager
import android.view.View


class HorarioActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horario)

        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        //viewPager.adapter = CustomPagerAdapter(this)
    }

}