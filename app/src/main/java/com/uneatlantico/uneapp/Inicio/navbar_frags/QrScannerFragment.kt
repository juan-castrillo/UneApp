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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.*
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.R
import kotlinx.android.synthetic.main.fragment_qr_scanner.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var qrResponseImage: ImageView
    private var lastText: String? = null
    private lateinit var b: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        formato.add("BarcodeFormat.QR_CODE")
        askPermision()
        //ask for location permision
        /*if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION)) { }
            else {
                ActivityCompat.requestPermissions(this.activity, Array(2){ Manifest.permission.ACCESS_COARSE_LOCATION }, 1)
            }
        }*/

        //Log.d("holaaaa", "funcionan los logs")
        //checkWifiUneat()
        val v = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        init(v)


        return v
    }

    /**
     * permission true -> location
     * permission false -> camera
     */
    private fun askPermision(){

        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this.activity, Array(2) { Manifest.permission.ACCESS_COARSE_LOCATION }, 1)
            }
        }
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this.activity, Array(2) { Manifest.permission.CAMERA }, 1)
            }
        }



    }

    override fun onResume() {
        super.onResume()

        barcodeScannerView.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeScannerView.pause()
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }*/

    private fun init(v: View) {
        b = v.findViewById(R.id.button) as Button
        b.setOnClickListener(this)

        qrResponseImage = v.findViewById(R.id.qr_recibido_imagen) as ImageView
        qrResponseImage.alpha = 0f

        barcodeScannerView = v.findViewById(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
        barcodeScannerView.setStatusText(" ")
        val formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeScannerView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeScannerView.decodeContinuous(callback)

    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                return
            }

            lastText = result.text
            barcodeScannerView.pause()
            qr_recibido_imagen.alpha = 1f
            handleQrResult(lastText.toString())
            barcodeScannerView.visibility = View.GONE

        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    //TODO comprobar si el usuario está en la red de la universidad antes de permitir abrir el scanner
    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> {
                Log.d("boton escaneo", "has pulsado mi boton")
                qrResponseImage.alpha = 0f
                barcodeScannerView.resume()
                barcodeScannerView.visibility = View.VISIBLE
            }
            else ->Log.d("boton escaneo", "NO has pulsado MI boton")
        }


    }

    //TODO llamar a este metodo
    fun checkWifiUneat():Boolean{
        var redCorrecta = false
        try {
            var wifiSSID:String
            val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE)as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                wifiSSID = wifiInfo.ssid
            }
            else
                wifiSSID = "No hay conexion"


            when (wifiSSID) { //TODO eliminar "AndroidWifi", "si", "wilkswifi" cuando salga a release
                "\"wuneat-becarios\"", "\"wuneat-alum\"", "\"wifiuneat-publica\"", "\"wifiuneat-pas\"", "\"AndroidWifi\"",
                "\"si\"", "\"wilkswifi\"" -> redCorrecta = true
            }

            if (redCorrecta)
                //initiateQrScanner()
            else
                mensaje("Debes estar conectado a la red de la universidad, usted está conectado a ${wifiSSID}", "Alerta Escaner QR")
        }
        catch (e: Exception){
            mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")
        }
        return redCorrecta
    }

    //TODO BORRAR METODO
    /**
     * DEPRECATED -----------------
     */
    fun initiateQrScanner(){

        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setCameraId(0)
        integrator.setPrompt(" ")
        //CaptureActivity.
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(true)// Use a specific camera of the device
        integrator.initiateScan(formato)
    }

    /**
     * Devuelve el resultado del escaneo de qr
     */
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }*/

    /**
     * Formateo el resultado de la lectura de qr
     * Compruebo si el formato es el adecuado
     */
    private fun handleQrResult(qrContents: String) {
        val partes = qrContents.split('_')

        try {

            var idEvento: String = partes[0]
            var fecha: String = partes[1]
            if(comprobarFecha(fecha)) {
                val listaQR: List<String> = listOf(idEvento, fecha)
                Log.d("listaQR antes de mandar", listaQR.toString())
                mensaje("Recibido", "QR respuesta")
                insertarRegistroQr(listaQR)
            }
            else mensaje("QR expirado", "QR respuesta")
            //insertarRegistroQr(idEvento.toLong(), fecha)
        }
        catch (z: Exception) {
            //Toast.makeText(this.context, "no compatible", Toast.LENGTH_SHORT)//TODO hacer algo cuando el qr no cumpla los parámetros adecuados
            Log.d("excepcionhandleQRresult", z.message)
            mensaje("no compatible")
        }
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

    private fun insertarRegistroQr(listaQR:List<String>){
        PostSend(listaQR, this.context)
    }
    /**
     * DEPRECATED
     * Inserto los datos del qr en base de datos
     */
    /*private fun insertarRegistroQr(idEvento: Long, fecha: String) {
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
    }*/

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
