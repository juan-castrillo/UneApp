package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.select

//TODO select a la base de datos sqllite para coger todos los registros y mostrarlos en esta actividad
class RegistroAsistenciaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_asistencia)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
        getRegistros()
    }

    private fun getRegistros(){
        val db = RegistrosDataBase(this)
        lateinit var algo: HashMap<String, String>
        lateinit var hola: String
        db.use { select("Registros", "*")}//.parseList(algo= HashMap(it)) }


    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
