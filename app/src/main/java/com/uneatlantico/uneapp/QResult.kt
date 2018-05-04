package com.uneatlantico.uneapp

import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.db.UneAppExecuter
import kotlinx.android.synthetic.main.fragment_qr_scanner.*
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class QResult{
    private var ct:Context
    /**
     * 1 Insertar
     * 2 Url
     * 3 Contenidos
     * 4 red no de la uni
     * 5 qrexpirado
     */
    var tipo:Int = 0
    constructor(ct:Context){
        this.ct = ct
    }
    fun checkWifiUneat():Boolean{
        var redCorrecta = false
        try {
            var wifiSSID:String
            val wifiManager = ct.getSystemService(Context.WIFI_SERVICE)as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                wifiSSID = wifiInfo.ssid
            }
            else
                wifiSSID = "ninguna red"

            when (wifiSSID) { //TODO eliminar "AndroidWifi", "si", "wilkswifi" cuando salga a release
                "\"wuneat-becarios\"", "\"wuneat-becarios-5g\"", "\"wuneat-alum\"", "\"wifiuneat-publica\"", "\"wifiuneat-pas\"", "\"AndroidWifi\"",
                "\"si\"", "\"wilkswifi\"" -> redCorrecta = true
            }

            if (redCorrecta)
            //initiateQrScanner()
            else{
                //mensaje("Debes estar conectado a la red de la universidad, usted está conectado a ${wifiSSID}", "Alerta Escaner QR")
                tipo = 4
            }
        }
        catch (e: Exception){
            //mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")
            Log.d("exceptionwifi", e.message)
            tipo = 4
        }
        return redCorrecta
    }

    /**
     * Formateo el resultado de la lectura de qr
     * Compruebo si el formato es el adecuado
     */
    fun handleQrResult(qrContents: String):Int {

        try {
            val partes = qrContents.split('_')
            val idEvento: String = partes[0]
            val fecha: String = partes[1]
            if(comprobarFecha(fecha) && checkWifiUneat()) {
                val listaQR: List<String> = listOf(idEvento, fecha)
                tipo = 1
                /*qrResponseImage.setImageResource(R.drawable.tick_enviado)
                qr_recibido_imagen.alpha = 1f*/

                insertarRegistroQr(listaQR)
            }

            //TODO poner imagen con una x
            else {
                tipo = 5
                /*mensaje("QR expirado", "QR respuesta")

                qrResponseImage.setImageResource(R.drawable.red_cross)
                qr_recibido_imagen.alpha = 1f*/
            }
        }

        //TODO poner imagen con una x
        catch (z: Exception) {
            analizeQr(qrContents)
            Log.d("excepcionhandleQRresult", z.message)
            //mensaje("no compatible")
        }
        return tipo
    }

    private fun analizeQr(qrContents: String){
        try{
            val partes = qrContents.split(':')
            if(partes[0] == "http" || partes[0] == "https"){
                tipo = 2
            }
            else{
               tipo = 3// mensaje(qrContents,"Contenido QR")
            }
        }catch (e:Exception){}
    }



    private fun comprobarFecha(fechaQR: String): Boolean {
        var aprovado = false
        val dateFormat = SimpleDateFormat("dd/MM/yyyy/HH:mm:ss", Locale("es", "ES"))
        val fechaQRs = dateFormat.parse(fechaQR)
        val fechaAC = dateFormat.parse(getDateTime())
        val interval = getDateDiff(fechaQRs, fechaAC, TimeUnit.SECONDS)
        if(!(interval > 15))
            aprovado = true
        //Log.d("tiempoActual", interval.toString())
        return aprovado
    }

    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        var diffInMillies:Long
        if(date2.time > date1.time) diffInMillies = date2.time - date1.time
        else diffInMillies = date1.time - date2.time
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    private fun getDateTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy/HH:mm:ss", Locale("es", "ES"))
        val date = Date()
        return dateFormat.format(date)
    }

    private fun insertarRegistroQr(listaQR:List<String>) {
        val post = PostSend()
        doAsync {
            val ultimoRegistro = UneAppExecuter.ultimoRegistro(ct)
            Log.d("check1", ultimoRegistro.idEvento.toString() + " != " + listaQR[0])
            if (ultimoRegistro.idEvento.toString() != listaQR[0]) {//idEvento
                if (ultimoRegistro.estado == 1) { //fecha
                    //TODO mandar a insertar un registro sin validar y preguntar al usuario
                    post.crearRegistro(listOf(ultimoRegistro.idEvento.toString(), ultimoRegistro.fecha), ct)
                    post.registrarAlumno(listaQR, ct)
                }
                else{
                    post.registrarAlumno(listaQR, ct)
                }
            }
            else {
                PostSend(listaQR, ct)
                Log.d("vine aca", "y no hice nada")
            }
        }
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(ct)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }
}