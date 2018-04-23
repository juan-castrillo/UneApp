package com.uneatlantico.uneapp.Inicio.navbar_frags.extra_frag_qrscanner

import android.content.Context
import android.util.Log
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.io.OutputStreamWriter
import java.io.BufferedReader
import java.io.InputStreamReader


class PostSend{
    lateinit var listaPost: List<String>
    constructor(listaD:List<String>, ct:Context){
        registrarAlumno(listaD, ct)
    }

    //cuando viene desde qr Fragment
    fun registrarAlumno(listaQR:List<String>, ct:Context){
        Log.d("registraralumno", "entro")
        val miListaTemp:List<String> = initiateList(listaQR, ct)//.toMutableList()

        val enviado = sendPostRequest(miListaTemp)
        //saveInDB(miListaTemp, ct, enviado)
        saveInDB(miListaTemp, ct, enviado)
        Log.d("registraralumno", miListaTemp.toString())
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
                            "estado" to estadoDB(listaInsert[3].toInt()) //estado(ct, listaInsert[0].toInt())
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
    fun idPersona():String{
        return "juan.castrillo"
    }

    fun estadoDB(estado:Int): Int {
        var estadoKul:Int
        when(estado){
            1 -> {
                estadoKul = 0
            }
            0 -> {
                estadoKul = 1
            }
            else -> {
                estadoKul = 1
            }
        }
        return estadoKul
    }

    /*fun estado(ct:Context, idEvento:Int, fecha: String):Int{
        val db:Int = RegistrosDataBase(ct).estadoUltimo(idEvento, fecha)
        var estado:Int
        when(db){
            1 -> {
                estado = 0
            }
            0 -> {
                estado = 1
            }
            else -> {
                estado = 1
            }
        }
        return estado
    }*/

    fun initiateList(listaQR:List<String>, ct:Context):List<String>{
        val fecha = formatfecha(listaQR[1])
        var listaInsert:List<String> = listOf(idPersona(), listaQR[0], fecha, (RegistrosDataBase(ct).estadoUltimo(listaQR[0].toInt(), formatfecha(listaQR[1], false)).toString()))//estado(ct, listaQR[0].toInt(), fecha).toString())
        //idPersona, idEvento, fecha, valido, validado, tipoRegistro, esPar
        Log.d("listaInsert" , listaInsert.toString())
        return listaInsert
    }

    private fun formatfecha(fechaNoFormat:String, i:Boolean = true):String {
        var trozosFecha = fechaNoFormat.split('/')
        var fechaFormat:String

        if(i)
            fechaFormat = trozosFecha[2] + "-" + trozosFecha[1] + "-" + trozosFecha[0] + " " + trozosFecha[3]
        else
            fechaFormat = trozosFecha[2] + "-" + trozosFecha[1] + "-" + trozosFecha[0]

        return fechaFormat
    }

    //TODO cambiar de http a https
    fun sendPostRequest(postList: List<String>):Boolean {
        var enviado:Boolean = false
        doAsync {

            try {
                val url = URL("http://uneasistencias.uneatlantico.es/registrar")
                val conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type","application/json")
                conn.setRequestProperty("Accept", "application/json")
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

                val outputStream = conn.outputStream
                val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
                outputStreamWriter.write(jsonParam.toString())
                outputStreamWriter.flush()
                outputStreamWriter.close()

                val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))

                var line: String? = null
                val sb = StringBuilder()

                while ((bufferedReader.readLine()) != null) {
                    line = bufferedReader.readLine()
                    sb.append(line)
                }

                bufferedReader.close()
                Log.d("Respuestacreo", sb.toString())
                enviado = true
            }
            catch (e: Exception){
                Log.d("responseWebservice", e.message)
            }
        }
        return enviado
    }
}