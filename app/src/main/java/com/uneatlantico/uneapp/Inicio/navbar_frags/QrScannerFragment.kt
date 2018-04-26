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
import com.journeyapps.barcodescanner.camera.CameraParametersCallback
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.R
import kotlinx.android.synthetic.main.fragment_qr_scanner.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.journeyapps.barcodescanner.camera.CameraSettings;


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment(), View.OnClickListener {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var qrResponseImage: ImageView
    private var lastText: String? = null
    private lateinit var b: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        askPermision()

        val v = inflater.inflate(R.layout.fragment_qr_scanner, container, false)

        qrResponseImage = v.findViewById(R.id.qr_recibido_imagen) as ImageView
        qrResponseImage.alpha = 0f
        if(checkWifiUneat())
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

    private fun init(v: View) {
        b = v.findViewById(R.id.button) as Button
        b.setOnClickListener(this)

        barcodeScannerView = v.findViewById(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
        barcodeScannerView.setStatusText(" ")
        barcodeScannerView.viewFinder
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
                //mensaje("Recibido", "QR respuesta")
                qr_recibido_imagen.alpha = 1f
                insertarRegistroQr(listaQR)
            }

            //TODO poner imagen con una x
            else mensaje("QR expirado", "QR respuesta")
            //insertarRegistroQr(idEvento.toLong(), fecha)
        }

        //TODO poner imagen con una x
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

    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(this.context)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }
}
