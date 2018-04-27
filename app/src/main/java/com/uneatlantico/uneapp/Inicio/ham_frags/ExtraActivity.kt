package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverProgreso


/**
 * https://github.com/akexorcist/Android-RoundCornerProgressBar
 */
class ExtraActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)

        val db = devolverProgreso(this)

        recyclerView = findViewById(R.id.bars_list)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val extraAdapter = ExtraAdapter(db)
        recyclerView.adapter = extraAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
