package com.uneatlantico.uneapp.db

import android.content.Context
import android.database.Cursor
import android.util.Log
import org.jetbrains.anko.doAsync

class UneAppExecuter{
    companion object {
        fun recogerAllRegistros(ct: Context): ArrayList<Registro> {
            val registros = ArrayList<Registro>()
            doAsync {
                //Log.d("rutaDB", DB_PATH)

                val db = UneAppDB(ct).readableDatabase
                lateinit var cursor: Cursor

                try {
                    cursor = db.rawQuery("select e.nombreEvento idEvento,r.fecha fecha,r.enviado enviado,r.estado estado from registros r, eventos e where r.idEvento = e._id", null)
                } catch (e: Exception) {
                    Log.d("queryRecogerAll", e.message)
                }
                try {
                    var Evento: String
                    var fecha: String
                    var estado: Int
                    var enviado: Int
                    if (cursor.moveToFirst())
                        while (!cursor.isAfterLast) {
                            Evento = cursor.getString(cursor.getColumnIndex("idEvento"))
                            fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                            estado = cursor.getInt(cursor.getColumnIndex("estado"))
                            enviado = cursor.getInt(cursor.getColumnIndex("enviado"))

                            registros.add(Registro(Evento, fecha, estado = estado, enviado = enviado))
                            cursor.moveToNext()
                        }
                } catch (x: Exception) {
                    Log.d("asignacionRecogerAll", x.message)
                }
                cursor.close()

            }
            return registros
        }

        fun estadoUltimo(ct: Context, idEvento: Int, fechahoy:String):Int{
            var estado: Int = 0
            //lateinit var cursor: Cursor
            val db = UneAppDB(ct).readableDatabase
            try {
                val cursor = db.rawQuery("select estado from registros WHERE idEvento = $idEvento AND fecha LIKE '%$fechahoy%' ORDER BY id DESC LIMIT 1", null)

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



        fun saveInDB(listaInsert: List<String>, ct:Context){
            doAsync {
                val db = UneAppDB(ct)
                val writableDB = db.writableDatabase
                val sql = "INSERT INTO registros (idEvento, fecha, enviado, estado) VALUES ('${listaInsert[0]}', '${listaInsert[1]}', '${listaInsert[2]}', '${listaInsert[3]}')"
                Log.d("insertStatement", sql)
                writableDB.execSQL(sql)

                writableDB.close()
            }
        }
    }
}