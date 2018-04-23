package com.uneatlantico.uneapp.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

/**
 * https://github.com/Kotlin/anko/wiki/Anko-SQLite
 * https://stackoverflow.com/questions/17529766/view-contents-of-database-file-in-android-studio
 * Creacion de la base de datos con las tablas correspondientes.
 * Registros
 * Eventos
 */
class RegistrosDataBase(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "UneAppDatabase", null, 1){
    companion object {
        var instance: RegistrosDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): RegistrosDataBase {
            if (instance == null)
                instance = RegistrosDataBase(ctx.applicationContext)
            return instance!!
        }
    }

    //TODO chequear los nombres de campos de las tablas y si estan correctas
    //TODO cambiar el campo "idEvento" a Int
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
                "Registros", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "idEvento" to INTEGER,
                "fecha" to TEXT,
                "enviado" to INTEGER,
                "estado" to INTEGER
        )
        db.createTable(
                "Eventos", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "nombreEvento" to TEXT,
                "grupo" to TEXT,
                "nombreProfesor" to TEXT
        )
        db.rawQuery("CREATE VIEW RegistroAsistenciaView AS SELECT A.time AS Start, B.time AS Stop FROM time A, time B WHERE A.id+1=B.id AND A.bool=1 AND B.bool=0", null)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("Registros", true)
    }

    /*class RegistroDb(val registrosDataBase: RegistrosDataBase.instance,
                     val dataMapper:DbDataMapper = DbDataMapper(){
                         fun requestRegistro() = registrosDataBase.use {
                             select("Registros")
                         }        }
                   )*/

    //TODO mover esto de sitio y cambiar el diseño de query
    /**
     * DEPRECATED
     */
    fun recogerAllRegistros(): ArrayList<Registro> {
        val registros = ArrayList<Registro>()
        val db = writableDatabase
        lateinit var cursor: Cursor

        try {
            cursor = db.rawQuery("select * from Registros", null)
        }
        catch (e: Exception) {
        }
        try {
            var idEvento: Long
            var fecha: String
            if (cursor.moveToFirst())
                while (cursor.isAfterLast == false) {
                    idEvento = cursor.getString(cursor.getColumnIndex("idEvento")).toLong()
                    fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                    registros.add(Registro(idEvento, fecha, 1, 1))
                    cursor.moveToNext() }
        }
        catch (x: Exception) { }
        return registros
    }

    /**
     * Mostar nombreEvento + fecha
     */
   /* fun eventosRegistroAsistencia():ArrayList<String> {
        var registros:List<String>
        val db = writableDatabase
        try {
            val cursor = db.rawQuery("SELECT nombreEvento from Eventos where id = ")
        }
        return registros
    }*/

    /**
     * conseguir el ultimo estado para cierta materia
     */
    fun estadoUltimo(idEvento: Int, fechahoy:String):Int{
        var estado: Int = 0
        val db = writableDatabase
        //lateinit var cursor: Cursor
        try {
            val cursor = db.rawQuery("select estado from Registros WHERE idEvento = $idEvento AND fecha LIKE '%$fechahoy%' ORDER BY id DESC LIMIT 1", null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    estado = cursor.getString(cursor.getColumnIndex("estado")).toInt()
                    cursor.moveToNext() }
            Log.d("estadoultimoDB", estado.toString())
        }
        catch (e: Exception) {
            Log.d("error consiguiendo ultimo registro", e.message)}

        return estado
    }

    val Context.database: RegistrosDataBase
        get() = RegistrosDataBase.getInstance(applicationContext)
}

