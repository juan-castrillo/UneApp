package com.uneatlantico.uneapp.Inicio.ham_frags

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.uneatlantico.uneapp.R

class ExtraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
