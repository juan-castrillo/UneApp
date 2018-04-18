package com.uneatlantico.uneapp.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
        private var instance: RegistrosDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): RegistrosDataBase {
            if (instance == null)
                instance = RegistrosDataBase(ctx.applicationContext)
            return instance!!
        }
    }

    //TODO chequear los nombres de campos de las tablas y si estan correctas
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
                "Registros", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "idEvento" to TEXT,
                "fecha" to TEXT
        )
        db.createTable(
                "Eventos", true,
                "idMateria" to INTEGER + PRIMARY_KEY + UNIQUE,
                "nombreEvento" to TEXT,
                "grupo" to TEXT,
                "nombreProfesor" to TEXT
        )
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
    fun recogerAllRegistros(): ArrayList<Registro> {
        val registros = ArrayList<Registro>()
        val db = writableDatabase
        lateinit var cursor: Cursor

        try {
            cursor = db.rawQuery("select * from " + "Registros", null)
        }
        catch (e: Exception) {
        }
        try {
            var idEvento: Long
            var fecha: String

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast == false) {
                    idEvento = cursor.getString(cursor.getColumnIndex("idEvento")).toLong()
                    fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                    registros.add(Registro(idEvento, fecha))
                    cursor.moveToNext()
                }
            }
        }
        catch (x: Exception) {

        }

        return registros
    }

    val Context.database: RegistrosDataBase get() = RegistrosDataBase.getInstance(applicationContext)
}

