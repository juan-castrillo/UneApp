package com.uneatlantico.uneapp.db

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.checkProgresoExiste
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.estadoUltimo
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.getIdPersona
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.insertarProgreso
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.saveRegistroInDB
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.updateProgreso
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.updateRegistro
import com.uneatlantico.uneapp.db.estructuras_db.Progreso
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray



/**
 * Encargada de enviar los datos al web service
 * y guardarlos en memoria
 */
class PostSend{

    lateinit var listaPost: List<String>
    constructor(listaD:List<String>, ct:Context){
        registrarAlumno(listaD, ct)
    } constructor()

    /**
     * Manda el registro al webservice y lo inserta en la base de datos
     */
    fun registrarAlumno(listaQR:List<String>, ct:Context){
        Log.d("registraralumno", "entro")
        val miListaTemp:List<String> = initiateList(listaQR, ct)//.toMutableList()

        val enviado = sendPostRequest(miListaTemp, ct)

        //creo la lista para Insertar en db
        val listaEspecificaInsert:List<String> = listOf(miListaTemp[1], miListaTemp[2], enviado.toString(), estadoDB(miListaTemp[3].toInt()).toString())
        saveRegistroInDB(listaEspecificaInsert, ct)

        Log.d("registraralumno", miListaTemp.toString())
    }

    /**
     * consigue el idPersona de la cuenta de google
     */
    fun idPersona(ct: Context):String{
        val usuario = getIdPersona(ct)
        return usuario
    }

    /**
     * cambia el estado de formato db a server
     */
    fun estadoDB(estado:Int): Int {
        var estadoKul:Int
        when(estado){
            1 -> estadoKul = 0
            0 -> estadoKul = 1
            else -> estadoKul = 1
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

    /**
     * creo una lista con idPersona, idEvento, fecha, espar
     */
    fun initiateList(listaQR:List<String>, ct:Context):List<String>{
        val fecha = formatfecha(listaQR[1])
        val espar = estadoUltimo(ct ,listaQR[0].toInt(), formatfecha(listaQR[1], false)).toString()
        val listaInsert:List<String> =
                listOf(idPersona(ct),
                        listaQR[0],
                        fecha,
                        (espar))//estado(ct, listaQR[0].toInt(), fecha).toString())

        return listaInsert
    }

    fun listaUpdate(listaRegistro: List<String>, ct: Context):List<String> {
        val listaRegistroTemp:List<String> = listOf(idPersona(ct), listaRegistro[0], listaRegistro[1], estadoDB(listaRegistro[3].toInt()).toString())
        return listaRegistroTemp
    }

    fun toBoolean(cuestion:String):Boolean = (cuestion == "1")

    private fun formatfecha(fechaNoFormat:String, i:Boolean = true):String {
        val trozosFecha = fechaNoFormat.split('/')
        var fechaFormat:String

        if(i)
            fechaFormat = trozosFecha[2] + "-" + trozosFecha[1] + "-" + trozosFecha[0] + " " + trozosFecha[3]
        else
            fechaFormat = trozosFecha[2] + "-" + trozosFecha[1] + "-" + trozosFecha[0]

        return fechaFormat
    }

    //TODO cambiar de http a https
    fun sendPostRequest(postList: List<String>, ct: Context):Int {
        var enviado:Int = 0
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
                jsonParam.put("esPar", toBoolean(postList[3]))


                Log.d("JsonObject", jsonParam.toString())
                val outputStream = conn.outputStream
                val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
                outputStreamWriter.write(jsonParam.toString())
                outputStreamWriter.flush()
                outputStreamWriter.close()
                val text:String? = conn.inputStream.use { it.reader().use { reader -> reader.readText() } }

                if(text != null) {
                    Log.d("respuestaWS", text)

                    val jsonObject = JSONObject(text)

                    //list.add(jsonObject.getString("valid"))

                    if(jsonObject.getString("horasTotales") != null)
                        if(jsonObject.getString("horasAlumno") != null) {

                        val horasArray1 = jsonObject.get("horasAlumno") as JSONArray
                        val horasAlumno = (horasArray1.getJSONObject(0).getDouble("horas")).toFloat()
                        //val tsmresponse:Int  = jsonObject.get("horasAlumno") as Js


                        val horasArray2 = jsonObject.get("horasTotales") as JSONArray
                        val horasTotales = (horasArray2.getJSONObject(0).getDouble("horas")).toFloat()

                        val progreso = Progreso(idEvento = postList[1].toInt(), horasAlumno = horasAlumno, horasEventoTotales = horasTotales)

                        //Log.d("listaJson", horasAlumno= progreso.horasAlumno.toString() + " " +  progreso.horasEventoTotales.toString())

                        insertarResponse(ct, progreso)
                    }
                    enviado = 1
                }
            }
            catch (e: Exception){
                Log.d("responseWebservice", e.message)
            }
        }
        return enviado
    }

    private fun insertarResponse(ct: Context, response: Progreso) {
        Log.d("numeroidEventoCheck", response.idEvento.toString())
        if(checkProgresoExiste(response.idEvento, ct) > 0)
            updateProgreso(ct, response)

        else
            insertarProgreso(ct, response)
    }

    //TODO determinar si es necesario el registro completo o solo el idEvento y fecha
    fun renviarWebService(registro:List<String>, ct: Context) {
        if(registro[2].toInt() == 1) {
            val listaTemp = listaUpdate(registro, ct)
            if(sendPostRequest(listaTemp, ct) == 1)
                updateRegistro(registro)
        }
    }

}