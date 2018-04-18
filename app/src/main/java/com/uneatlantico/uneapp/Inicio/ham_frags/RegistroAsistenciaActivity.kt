package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select

//TODO select a la base de datos sqllite para coger todos los registros y mostrarlos en esta actividad
class RegistroAsistenciaActivity : AppCompatActivity() {
lateinit var hola:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_asistencia)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
        getRegistros()
    }

    private fun getRegistros(){
        val db = RegistrosDataBase(this)

        try {
            db.use {
                select("Registros", "idEvento", "fecha").exec {
                    parseList(object : MapRowParser<Map<String, Any?>> {
                        override fun parseRow(columns: Map<String, Any?>): Map<String, Any?> {
                            Log.e("Your result", columns.toString())
                            return columns
                        }
                    })
                }//.parseList(algo= HashMap(it)) }
            }

            //mensaje(hola)
        }
        catch (e:Exception){
            mensaje(e.toString())
        }

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

    /*abstract class something : RowParser<Triple<Int, String, String>> {
        override fun parseRow(columns: Array<Any>): Triple<Int, String, String> {
            return Triple(columns[0] as Int, columns[1] as String, columns[2] as String)
        }
    }*/
}
