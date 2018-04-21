package com.uneatlantico.uneapp.Inicio.navbar_frags

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.zxing.integration.android.IntentIntegrator
import com.uneatlantico.uneapp.Inicio.navbar_frags.extra_frag_qrscanner.PostSend
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.insert
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment(), View.OnClickListener {

    private var formato:ArrayList<String> = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        formato.add("BarcodeFormat.QR_CODE")

        //ask for location permision
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION)) { }
            else {
                ActivityCompat.requestPermissions(this.activity, Array(2){ Manifest.permission.ACCESS_COARSE_LOCATION }, 1)
            }
        }
        //Log.d("holaaaa", "funcionan los logs")
        checkWifiUneat()
        val b: Button = v.findViewById(R.id.button) as Button
        b.setOnClickListener(this)

        return v
    }

    //TODO comprobar si el usuario está en la red de la universidad antes de permitir abrir el scanner
    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> {
                Log.d("boton escaneo", "has pulsado mi boton")
                val tempInsert = listOf("1", "10/20/2018/12:34:34")
                Log.d("boton escaneo", tempInsert.toString())
                //insertarRegistroQr(tempInsert)
                //handleQrResult("1_20/04/2018/13:42:12")
                checkWifiUneat()
            }
            else ->Log.d("boton escaneo", "NO has pulsado MI boton")
        }


    }

    //TODO pedir peticiones de localizacion
    fun checkWifiUneat(){
        try {
            var wifiSSID:String
            val wifiManager = activity.getSystemService(Context.WIFI_SERVICE)as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                wifiSSID = wifiInfo.ssid
            }
            else
                wifiSSID = "No hay conexion"

            var redCorrecta = false
            when (wifiSSID) { //TODO eliminar "AndroidWifi", "si", "wilkswifi" cuando salga a release
                "\"wuneat-becarios\"", "\"wuneat-alum\"", "\"wifiuneat-publica\"", "\"wifiuneat-pas\"", "\"AndroidWifi\"",
                "\"si\"", "\"wilkswifi\"" -> redCorrecta = true
            }

            if (redCorrecta)
                initiateQrScanner()
            else
                mensaje("Debes estar conectado a la red de la universidad, usted está conectado a ${wifiSSID}", "Alerta Escaner QR")
        }
        catch (e: Exception){
            mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")
        }
    }

    fun initiateQrScanner(){

        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setCameraId(0)
        integrator.setPrompt(" ")
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(true)// Use a specific camera of the device
        integrator.initiateScan(formato)
    }

    /**
     * Devuelve el resultado del escaneo de qr
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result !== null) {
            if (resultCode == Activity.RESULT_OK) {
                if (result.contents == null)
                    //Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
                else {
                    //Toast.makeText(this.context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                    handleQrResult(result.contents)
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Formateo el resultado de la lectura de qr
     * Compruebo si el formato es el adecuado
     */
    private fun handleQrResult(qrContents: String) {
        val partes = qrContents.split('_')

        try {
            var idEvento: String = partes[0]
            var fecha: String = comprobarFecha(partes[1])

            val listaQR: List<String> = listOf(idEvento, fecha)

            Log.d("listaQR antes de mandar" , listaQR.toString())
            mensaje("Recibido", "QR respuesta")
            insertarRegistroQr(listaQR)
            //insertarRegistroQr(idEvento.toLong(), fecha)
        }
        catch (z: Exception) {
            //Toast.makeText(this.context, "no compatible", Toast.LENGTH_SHORT)//TODO hacer algo cuando el qr no cumpla los parámetros adecuados
            Log.d("excepcionhandleQRresult", z.message)
            mensaje("no compatible")
        }


    }

    private fun comprobarFecha(fecha: String): String {
        val tiempoActual:Date = Calendar.getInstance().time;
        Log.d("tiempoActual", tiempoActual.toString())
        var algo = fecha.split('/')
        val hora = algo[3]
        return fecha
    }

    private fun insertarRegistroQr(listaQR:List<String>){
        PostSend(listaQR, this.context)
    }
    /**
     * DEPRECATED
     * Inserto los datos del qr en base de datos
     */
    private fun insertarRegistroQr(idEvento: Long, fecha: String) {
        val db = RegistrosDataBase(this.context)
        try {
            db.use{
                insert(
                    "Registros",
                        "idEvento" to idEvento,
                        "fecha" to fecha
                )
            }
        }
        catch (xc: Exception){
            Log.d("no insertado", xc.message)
        }
    }

    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }

    /*override fun onDestroy() {
        super.onDestroy()
        if (mensaje() != null) {
            mensaje().dismiss()
            dialog = null
        }
    }*/
}// Required empty public constructor
