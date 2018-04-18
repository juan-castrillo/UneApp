package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg.RegistroAsistenciaAdapter
import com.uneatlantico.uneapp.R

//TODO select a la base de datos sqllite para coger todos los registros y mostrarlos en esta actividad
class RegistroAsistenciaActivity : AppCompatActivity() {

    /*lateinit var idEvento: Array<Long>
    lateinit var fecha: Array<String>
    lateinit var db: kotlin.collections.ArrayList<Registro>*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_asistencia)
        //db = RegistrosDataBase(this).recogerAllRegistros()
        //getRegistros()
        //idEvento = idEvento()
        //fecha = fecha()
        try {
            var recyclerView = findViewById<RecyclerView>(R.id.registro_asistencias_recyclerview)
            val registroAsistenciaAdapter = RegistroAsistenciaAdapter()
            recyclerView.adapter = registroAsistenciaAdapter
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
        }
        catch (e:Exception){

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    /*private fun getRegistros() {
        //lateinit var temp1: Array<Long>
        //lateinit var temp2: Array<String>
        var i= 0
        db.forEach {
            idEvento[i] = it.idEvento
            fecha[i] = it.fecha
            i++
        }
    }

    fun idEvento(): Array<Long> {
        lateinit var temp1: Array<Long>
        var i = 0
        db.forEach {
            temp1[i] = it.idEvento
            i++
        }
        return temp1
    }

    fun fecha(): Array<String> {
        lateinit var temp1: Array<String>
        var i = 0
        db.forEach {
            temp1[i] = it.fecha
            i++
        }
        return temp1
    }*/

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

    /*abstract class something : RowParser<Triple<Int, String, String>> {
        override fun parseRow(columns: Array<Any>): Triple<Int, String, String> {
            return Triple(columns[0] as Int, columns[1] as String, columns[2] as String)
        }
    }*/
}
