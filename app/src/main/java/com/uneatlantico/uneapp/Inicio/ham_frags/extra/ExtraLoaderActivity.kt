package com.uneatlantico.uneapp.Inicio.ham_frags.extra

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.estadoUltimo
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.idEventoPorNombre
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.recogerRegistros
import kotlinx.android.synthetic.main.small_card_layout.view.*
import org.jetbrains.anko.doAsync

class ExtraLoaderActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_loader)
        val postSend = PostSend()
        val evento = intent.getStringExtra("evento")
        val idEvento = idEventoPorNombre(this, evento)
        estadoUltimo(this, idEvento )

        val db = recogerRegistros(this, idEvento)
        recyclerView = findViewById(R.id.extra_materia_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val extraLoaderAdapter = ExtraLoaderAdapter(db, object : ExtraLoaderAdapter.ExtraLoaderAdapterListener {
            override fun cardOnClick(v: View, position: Int) {
                doAsync {
                    if(position < 20) {
                        Log.d("contenidoCartaRegistro", v.eventoTextView.text.toString() + " " + v.fechaTextView.text.toString())
                        if (v.card_view_registro.cardBackgroundColor != ColorStateList.valueOf(Color.WHITE))
                            if (postSend.renviarWebService(listOf(v.eventoTextView.text.toString(), v.fechaTextView.text.toString()), applicationContext) == 1)
                                v.card_view_registro.setCardBackgroundColor(Color.WHITE)
                    }
                    else {//TODO cargas las materias que quedan
                    }
                }
            }


            /*override fun iconImageUnFollowOnClick(v: View, position: Int) {}*/
        })
        recyclerView.adapter = extraLoaderAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = evento
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
