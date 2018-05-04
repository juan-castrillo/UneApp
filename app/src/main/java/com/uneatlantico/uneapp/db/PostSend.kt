package com.uneatlantico.uneapp.db

import android.content.Context
import android.util.Log
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.checkProgresoExiste
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.estadoUltimo
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.getIdPersona
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.getRegistro
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.idEventoPorNombre
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.insertarProgreso
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.saveRegistroInDB
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.ultimoRegistro
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.updateProgreso
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.updateRegistro
import com.uneatlantico.uneapp.db.estructuras_db.Progreso
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


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
        //checkNoDeslog(ct, listaQR)
        val miListaTemp: List<String> = initiateList(listaQR, ct)//.toMutableList()

        val enviado = sendPostRequest(miListaTemp, ct)

        //creo la lista para Insertar en db
        val listaEspecificaInsert: List<String> = listOf(miListaTemp[1], miListaTemp[2], enviado.toString(), estadoDB(miListaTemp[3].toInt()).toString())
        saveRegistroInDB(listaEspecificaInsert, ct)

        Log.d("registraralumno", miListaTemp.toString())

    }
    fun crearRegistro(listaQR:List<String>, ct:Context){
        Log.d("registraralumno", "entro")
        //checkNoDeslog(ct, listaQR)
        val miListaTemp: List<String> = initiateList(listaQR, ct, "0")//.toMutableList()

        val enviado = sendPostRequest(miListaTemp, ct)

        //creo la lista para Insertar en db
        val listaEspecificaInsert: List<String> = listOf(miListaTemp[1], miListaTemp[2], enviado.toString(), estadoDB(miListaTemp[3].toInt()).toString())
        saveRegistroInDB(listaEspecificaInsert, ct)

        Log.d("registraralumno", miListaTemp.toString())

    }
    /**
     * Si un usuario se ha quedado logueado en un evento desloguearlo sin verificacion y loguearlo en el nuevo
     */
    /*private fun checkNoDeslog(ct:Context, listaQR: List<String>):Int {
        var validado = 1
        val ultimoRegistro = ultimoRegistro(ct)
        if(ultimoRegistro.idEvento.toString() != listaQR[0]) //idEvento
            if(ultimoRegistro.estado == 1){ //fecha
                //TODO mandar a insertar un registro sin validar y preguntar al usuario
                validado = 0
            }
        return validado
    }*/

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

    /**
     * creo una lista con idPersona, idEvento, fecha, espar
     */
    fun initiateList(listaQR:List<String>, ct:Context, validado:String ="1"):List<String>{
        var fecha:String
        var espar:String
        if(validado != "0") {
            fecha = formatfecha(listaQR[1])
            espar = estadoUltimo(ct ,listaQR[0].toInt(), formatfecha(listaQR[1], false)).toString()
        }
        else {
            fecha = listaQR[1]
            espar = estadoUltimo(ct, listaQR[0].toInt(), fecha).toString()
        }
        val listaInsert:List<String> =
                listOf(idPersona(ct),
                        listaQR[0],
                        fecha,
                        (espar), validado)//estado(ct, listaQR[0].toInt(), fecha).toString())

        return listaInsert
    }

    fun listaUpdate(listaRegistro: List<String>, ct: Context):List<String> {
        val idEvento = idEventoPorNombre(ct, listaRegistro[0])
        val fullRegistro = getRegistro(idEvento, listaRegistro[1], ct)

        val listaRegistroTemp:List<String> = listOf(idPersona(ct), //idPersona
                fullRegistro.idEvento.toString(), //idEvento
                fullRegistro.fecha, //fecha
                estadoDB(fullRegistro.estado).toString()) //estado contrario al de base de datos para llamarWebService

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
            jsonParam.put("validado", postList[4].toInt())
            jsonParam.put("tipoRegistro", "Alumno")
            jsonParam.put("esPar", toBoolean(postList[3]))


            Log.d("JsonObject", jsonParam.toString())
            val outputStream = conn.outputStream
            val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
            outputStreamWriter.write(jsonParam.toString())
            outputStreamWriter.flush()
            outputStreamWriter.close()
            val text:String? = conn.inputStream.use { it.reader().use { reader -> reader.readText() } }

            var horasTotales:Float = 1.0F
            var horasAlumno:Float = 1.0F

            if(text != null) {
                Log.d("respuestaWS", text)

                val jsonObject = JSONObject(text)

                //list.add(jsonObject.getString("valid"))
                if(jsonObject.getString("valid") != null) {

                    try {
                        val horasArray1 = jsonObject.get("horasAlumno") as JSONArray
                        horasAlumno = (horasArray1.getJSONObject(0).getDouble("horas")).toFloat()
                    }
                    catch (e:Exception){}
                    try {
                        if(postList[1].toInt() == 220 || postList[1].toInt() == 221)
                            horasTotales = 20.0F
                        else {
                            horasTotales = 60.0F
                            /*val horasArray2 = jsonObject.get("horasTotales") as JSONArray
                            horasTotales = (horasArray2.getJSONObject(0).getDouble("horas")).toFloat()*/
                        }
                    }
                    catch (e:Exception){}

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
    fun renviarWebService(registro:List<String>, ct: Context):Int {
        var conseguido = 0
        Log.d("reenviarWebSErvicedatos", registro[0] + "  " + registro[1])

        val listaTemp = listaUpdate(registro, ct)
        val verification = sendPostRequest(listaTemp, ct)
        Log.d("esunoonopost", verification.toString())
        if (verification == 1) {
            updateRegistro(listaTemp, ct)
            conseguido = 1
        }

        return conseguido
    }

}