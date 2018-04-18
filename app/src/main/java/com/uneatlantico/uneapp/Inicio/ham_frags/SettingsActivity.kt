package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uneatlantico.uneapp.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
