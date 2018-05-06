package com.uneatlantico.uneapp.Inicio.navbar_frags

import android.Manifest
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
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.WebViewActivity
import com.uneatlantico.uneapp.db.PostSend
import com.uneatlantico.uneapp.db.UneAppExecuter
import kotlinx.android.synthetic.main.fragment_qr_scanner.*
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment(){

    /*private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var qrResponseImage: ImageView
    private var lastText: String? = null
    private lateinit var b: Button*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        askPermision()

        val v = inflater.inflate(R.layout.fragment_qr_scanner, container, false)



        //checkWifiUneat()
        init(v)

        return v
    }

    /**
     * permission for camera and location
     */
    private fun askPermision(){
        val permissionCamera:Int = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA)
        val permissionLocation:Int = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
        var listPermissionsNeeded = ArrayList<String>()
        if(permissionLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if(permissionCamera != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        if(!listPermissionsNeeded.isEmpty())
            ActivityCompat.requestPermissions(this.activity!!, listPermissionsNeeded.toTypedArray(), 0)
    }

    private fun init(v: View) {


        /*b = v.findViewById(R.id.button) as Button
        b.setOnClickListener(this)
        qrResponseImage = v.findViewById(R.id.qr_recibido_imagen) as ImageView
        qrResponseImage.setImageResource(R.drawable.tick_enviado)
        qrResponseImage.alpha = 0f

        val s:CameraSettings = CameraSettings()
        s.isExposureEnabled = false
        s.isMeteringEnabled = false

        //s.isScanInverted = false
        s.requestedCameraId = 0
        //val nolosetampoco = CameraManager
        //val algo = CameraPreview(this.context).cameraSettings.
        barcodeScannerView = v.findViewById(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
        barcodeScannerView.barcodeView.cameraSettings = s
        //barcodeScannerView.viewFinder.visibility = View.INVISIBLE
        barcodeScannerView.setStatusText(" ")
        val formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeScannerView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeScannerView.decodeContinuous(callback)*/


        //barcodeScannerView.barcodeView.cameraDistance = 100.0f
        //barcodeScannerView.pause()

    }

    /*/override fun onResume() {
        super.onResume()

        barcodeScannerView.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeScannerView.pause()
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            barcodeScannerView.pause()
            barcodeScannerView.visibility = View.GONE
            if (result.text == null) {
                return
            }
            lastText = result.text
            handleQrResult(lastText.toString())
        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> {

                qrResponseImage.alpha = 0f
                barcodeScannerView.visibility = View.VISIBLE
                barcodeScannerView.resume()

            }
            else -> Log.d("holahola", "adioszxd")
        }
    }*/

    //TODO llamar a este metodo
    fun checkWifiUneat():Boolean{
        var redCorrecta = false
        try {
            var wifiSSID:String
            val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE)as WifiManager
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
                mensaje("Debes estar conectado a la red de la universidad, usted está conectado a ${wifiSSID}", "Alerta Escaner QR")
            }
        }
        catch (e: Exception){
            mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")
            Log.d("exceptionwifi", e.message)
        }
        return redCorrecta
    }

    /**
     * Formateo el resultado de la lectura de qr
     * Compruebo si el formato es el adecuado
     */
    private fun handleQrResult(qrContents: String) {

        try {
            val partes = qrContents.split('_')
            val idEvento: String = partes[0]
            val fecha: String = partes[1]
            if(comprobarFecha(fecha) && checkWifiUneat()) {
                val listaQR: List<String> = listOf(idEvento, fecha)



                insertarRegistroQr(listaQR)
            }

            //TODO poner imagen con una x
            else {
                mensaje("QR expirado", "QR respuesta")

            }
        }

        //TODO poner imagen con una x
        catch (z: Exception) {
            analizeQr(qrContents)
            Log.d("excepcionhandleQRresult", z.message)
            //mensaje("no compatible")
        }
    }

    private fun analizeQr(qrContents: String){
        try{
            val partes = qrContents.split(':')
            if(partes[0] == "http" || partes[0] == "https"){
                startUrlAlert(qrContents)
            }
            else{
                mensaje(qrContents,"Contenido QR")
            }
        }catch (e:Exception){}
    }

    private fun startUrlAlert(qrContents: String) {
        val builder = AlertDialog.Builder(this.context!!)
        builder.setMessage("Desea usted abrir \"$qrContents\"?")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    Toast.makeText(this.context!!, "abriendo $qrContents", Toast.LENGTH_SHORT).show()
                    val i = Intent(this.context, WebViewActivity::class.java)
                        i.putExtra("url", qrContents)
                        i.putExtra("titulo", "QR")
                        startActivityForResult(i, 0)
                }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
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
        val ct = this.context!!
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



    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(this.context!!)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }
}
