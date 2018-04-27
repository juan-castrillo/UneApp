package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg.RegistroAsistenciaAdapter
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.recogerRegistros
import com.uneatlantico.uneapp.db.estructuras_db.Registro
import kotlinx.android.synthetic.main.small_card_layout.view.*

//TODO select a la base de datos sqllite para coger todos los registros y mostrarlos en esta actividad
class RegistroAsistenciaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_asistencia)
        val postSend = PostSend()
        val db = recogerRegistros(this)
        getRegistros(db)

        recyclerView = findViewById(R.id.registro_asistencias_recyclerview)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //val registroAsistenciaAdapter = RegistroAsistenciaAdapter(db)
        val registroAsistenciaAdapter = RegistroAsistenciaAdapter(db, object : RegistroAsistenciaAdapter.RegistroAsistenciaAdapterListener {
            override fun cardOnClick(v: View, position: Int) {
                Log.d("contenidoCartaRegistro", v.eventoTextView.text.toString() + " " + v.fechaTextView.text.toString())
                postSend.renviarWebService(listOf(v.eventoTextView.text.toString(), v.fechaTextView.text.toString()), applicationContext)
            }

            /*override fun iconImageViewOnClick(v: View, position: Int) {

            }

            override fun iconImageUnFollowOnClick(v: View, position: Int) {

            }*/
        })
        recyclerView.adapter = registroAsistenciaAdapter


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    private fun getRegistros(a:List<Registro>) {
        var i = 0
        a.forEach { Log.d("numeroFila " + i.toString()+ " : ", it.fecha); i++ }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }
}
