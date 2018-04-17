package com.uneatlantico.uneapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * https://github.com/Kotlin/anko/wiki/Anko-SQLite
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
                "id" to INTEGER + PRIMARY_KEY + UNIQUE + AUTOINCREMENT,
                "idEvento" to TEXT,
                "fecha" to TEXT
        )
        db.createTable(
                "Eventos", true,
                "idMateria" to INTEGER + PRIMARY_KEY + UNIQUE + AUTOINCREMENT,
                "nombreEvento" to TEXT,
                "grupo" to TEXT,
                "nombreProfesor" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("Registros", true)
    }
}

val Context.database: RegistrosDataBase get() = RegistrosDataBase.getInstance(applicationContext)