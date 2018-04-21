package com.uneatlantico.uneapp.Inicio.navbar_frags.extra_frag_qrscanner

import android.content.Context
import android.util.Log
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class PostSend{
    lateinit var listaPost: List<String>
    constructor(listaD:List<String>, ct:Context){
        registrarAlumno(listaD, ct)
    }

    //cuando viene desde qr Fragment
    fun registrarAlumno(listaQR:List<String>, ct:Context){
        Log.d("registrar alumno", "entro")
        val miListaTemp:List<String> = initiateList(listaQR, ct)//.toMutableList()

        val enviado = sendPostRequest(miListaTemp)
        //saveInDB(miListaTemp, ct, enviado)
        saveInDB(miListaTemp, ct, enviado)
        Log.d("registrar alumno", miListaTemp.toString())
    }

    fun saveInDB(listaInsert: List<String>, ct:Context, enviado:Boolean = false){
        doAsync {
            val db = RegistrosDataBase(ct)
            try {
                db.use{
                    //TODO preguntar si tengo que pasar el estado actual o el que quiero a que cambie
                    insert(
                            "Registros",
                            "idEvento" to listaInsert[1].toInt(),
                            "fecha" to listaInsert[2],
                            "enviado" to enviado,
                            "estado" to listaInsert[3] //estado(ct, listaInsert[0].toInt())
                    )
                }
            }
            catch (xc: Exception){
                Log.d("No insertado", xc.message)
                //Toast.makeText(this.context, "no insertado", Toast.LENGTH_SHORT)
            }
        }
    }

    //TODO conseguir el idPersona
    fun idPersona():Int{
        return 1
    }

    /*fun estado(ct:Context, idEvento:Int): Int {
        var estado = 0
        doAsync {
            val db = RegistrosDataBase(ct).writableDatabase

            lateinit var cursor: Cursor
            try {
                db.use{
                    //cursor = rawQuery("SELECT estado FROM Registros WHERE idEvento = ? ORDER BY id DESC LIMIT 1", arrayOf(idEvento.toString()))
                    cursor = db.rawQuery("select estado from Registros WHERE idEvento = $idEvento ORDER BY id DESC LIMIT 1", null)
                }
                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        when(cursor.getInt(cursor.getColumnIndex("estado"))){
                            1 -> estado = 0
                            0 -> estado = 1
                            else -> estado = 1
                        }
                        cursor.moveToNext()
                    }
                cursor.close()
            }
            catch (xc: Exception){Log.d("Select estado", xc.message)}
            /*try {

            }
            catch (e:Exception){Log.d("Ajustar estado", e.message)}*/
        }
        return estado
    }*/

    fun estado(ct:Context, idEvento:Int):Int{
        val db:Int = RegistrosDataBase(ct).estadoUltimo(idEvento)
        var estado:Int
        when(db){
            1 -> estado = 0
            0 -> estado = 1
            else -> estado = 1
        }
        return estado
    }

    fun initiateList(listaQR:List<String>, ct:Context):List<String>{
        var listaInsert:List<String> = listOf<String>(idPersona().toString(), listaQR[0], listaQR[1], estado(ct, listaQR[0].toInt()).toString())
        /*listaInsert[0] = idPersona().toString() //idPersona
        listaInsert[1] = listaQR[0] //idEvento
        listaInsert[2] = listaQR[1] //fecha
        listaInsert[3] = estado(ct, listaQR[0].toInt()).toString() //espar*/
        Log.d("listaInsert" , listaInsert.toString())
        return listaInsert
    }

    fun sendPostRequest(postList: List<String>):Boolean {
        var enviado:Boolean = false
        doAsync {

            try {
                val url = URL("https://uneasistencias.uneatlantico.es/registrar")
                val conn = url.openConnection() as HttpsURLConnection
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true

                val jsonParam = JSONObject()
                jsonParam.put("idPersona", postList[0])
                jsonParam.put("idEvento", postList[1])
                jsonParam.put("fecha", postList[2])
                jsonParam.put("valido", true)
                jsonParam.put("validado", 1)
                jsonParam.put("tipoRegistro", "Alumno")
                jsonParam.put("esPar", postList[3].toBoolean())

                Log.d("JSON", jsonParam.toString())
                val os = DataOutputStream(conn.outputStream)
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString())

                os.flush()
                os.close()

                Log.d("STATUS", (conn.responseCode).toString())
                Log.d("MSG", conn.responseMessage)
                if(conn.responseMessage != null)
                    enviado = true

                conn.disconnect()
            }
            catch (e: Exception){
                Log.d("response Web service", e.message)
            }
        }
        return enviado
    }
}