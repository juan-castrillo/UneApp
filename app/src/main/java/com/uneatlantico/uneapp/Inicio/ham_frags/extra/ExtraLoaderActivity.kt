package com.uneatlantico.uneapp.Inicio.ham_frags.extra

import android.app.FragmentManager
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
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.recogerFechasEvento
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.recogerRegistros
import com.uneatlantico.uneapp.db.estructuras_db.Registro
import kotlinx.android.synthetic.main.small_card_layout.view.*
import org.jetbrains.anko.doAsync

class ExtraLoaderActivity : AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private val fm = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_loader)
        val postSend = PostSend()

        val evento = intent.getStringExtra("evento")
        val idEvento = idEventoPorNombre(this, evento)
        Log.d("hello", idEvento.toString())
        estadoUltimo(this, idEvento )

        val db = registrosGroupByFecha(recogerFechasEvento(applicationContext, idEvento))
        recyclerView = findViewById(R.id.extra_materia_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val extraLoaderAdapter = ExtraLoaderAdapter(db,evento, object : ExtraLoaderAdapter.ExtraLoaderAdapterListener {
            override fun cardOnClick(v: View, position: Int) {
                doAsync {
                    if(position < 20) {
                        Log.d("contenidoCartaRegistro", v.eventoTextView.text.toString() + " " + v.fechaTextView.text.toString())
                        if (v.card_view_registro.cardBackgroundColor != ColorStateList.valueOf(Color.WHITE)) {
                            if (postSend.renviarWebService(listOf(v.eventoTextView.text.toString(), v.fechaTextView.text.toString()), applicationContext) == 1)
                                v.card_view_registro.setCardBackgroundColor(Color.WHITE)
                        }
                        else{
                            val bundle = Bundle()
                            bundle.putInt("idEvento", idEvento)
                            bundle.putString("fecha", db[position].fecha)

                            val bottomEvento = RegistrosListDialogFragment()
                            bottomEvento.arguments = bundle
                            bottomEvento.show(fm, bottomEvento.tag)

                        }

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

    /**
     * Mantener ultimo registro o alguno de ellos
     */
    private fun registrosGroupByFecha(registros:ArrayList<Registro>):ArrayList<Registro>{
        val registrosU = ArrayList<Registro>()

        var fechaTemp = ""
        var enviado = 0

        registros.forEach {

            val fechaTemp1 = formatfecha(it.fecha)
            if(it.enviado == 1) {
                enviado = 1
            }
            if(fechaTemp1 != fechaTemp) {
                fechaTemp = fechaTemp1
                registrosU.add(Registro(fecha = fechaTemp, enviado = enviado))
                enviado = 0

            }
        }

        return registrosU
    }

    private fun formatfecha(fechaNoFormat:String):String {
        val trozosFecha = fechaNoFormat.split(' ')
        return trozosFecha[0]
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
