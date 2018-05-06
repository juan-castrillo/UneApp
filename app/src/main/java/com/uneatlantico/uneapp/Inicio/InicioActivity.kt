package com.uneatlantico.uneapp.Inicio

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.uneatlantico.uneapp.Inicio.navbar_frags.CampusFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.HorarioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.InicioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.QrScannerFragment
import com.uneatlantico.uneapp.R
import android.widget.*
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.camera.CameraInstance
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.uneatlantico.uneapp.Notifications
import com.uneatlantico.uneapp.QResult
import com.uneatlantico.uneapp.WebViewActivity
import java.util.*


class InicioActivity : AppCompatActivity() {

    private val qResult = QResult(this)
    //Menu lateral de hamburguesa
    //private lateinit var googleAccount: GoogleSignInAccount
    //private lateinit var googleAccount: List<String>
    private lateinit var mName: TextView
    private lateinit var mImage: ImageView
    private lateinit var mMail: TextView
    private lateinit var headerView: View



    private lateinit var menuImageView: ImageView
    private lateinit var qrSlideScanner: DecoratedBarcodeView
    private val s: CameraSettings = CameraSettings()
    private var lastText: String? = null

    //TODO eliminar estas variables
    //fragmentos para la barra de navegacion inferior
    private var inicioFragment = InicioFragment.newInstance()
    private val fm = supportFragmentManager
    private val menuFragment = MenuFragment.newInstance()
    private lateinit var toolbar: Toolbar
    private var doubleBackToExitPressedOnce = false
    private val mHandler = Handler()

    //private lateinit var bottomSheet:BottomSheetLayout
    private lateinit var bottomSheetFragment:BottomSheetDialogFragment
    private lateinit var bottomSheet:LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var greyContainer:FrameLayout

    /**
     * https://github.com/umano/AndroidSlidingUpPanel
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        askPermision()
        //googleAccount = intent.extras.getParcelable("account")
        //googleAccount = devolverUsuario(this)

        openFragment(QrScannerFragment.newInstance())

        /*val dialog = TopSheetDialog(this)
dialog.setContentView(R.layout.fragment_menu)
dialog.show()*/
        /*val sheet = findViewById<View>(R.id.container)
TopSheetBehavior.from(sheet).setState(TopSheetBehavior.STATE_EXPANDED);*/
        val ct = this
        greyContainer = findViewById(R.id.greycontainer)
        qrScanner()
        bottomSheet = findViewById(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        if (s.requestedCameraId != 0) {
                            qrScanner(0)
                        }

                        qrSlideScanner.resume()
                        //greyContainer.alpha = 0.5F
                        //btnBottomSheet.setText("Close Sheet")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                        qrScanner()
                        //qrSlideScanner.pause()
                        //greyContainer.alpha = 0F
                        //btnBottomSheet.setText("Expand Sheet")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {


                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                if (slideOffset < 0.5)
                    greyContainer.alpha = slideOffset
                else greyContainer.alpha = 0.5F
            }
        })

        bottomSheetFragment = MenuFragment()
        //bottomSheetFragment.show(fm, bottomSheetFragment.tag)


        //toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = " "

        /*greyContainer.setOnClickListener {
            if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }*/

        menuImageView = findViewById(R.id.menuImage)
        menuImageView.setOnClickListener {
            /*if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                //btnBottomSheet.setText("Close sheet");
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                //btnBottomSheet.setText("Expand sheet");
            }*/
            //notifications.showNotification("hola", "hola")

            //notification("hola", "hola")
            bottomSheetFragment.show(fm, bottomSheetFragment.tag)
        }
        CameraInstance(this).close()
        //All sobre la barra de navegacion inferior
    }

    private fun askPermision(){
        val permissionCamera:Int = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val permissionLocation:Int = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        var listPermissionsNeeded = ArrayList<String>()
        if(permissionLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if(permissionCamera != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        if(!listPermissionsNeeded.isEmpty())
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 0)
    }

    private fun qrScanner(cameraId:Int = 10) {

            //s.isExposureEnabled = false
            //s.isMeteringEnabled = false

            //s.isScanInverted = false
            s.requestedCameraId = cameraId
            qrSlideScanner = findViewById(R.id.qr_slide_scanner)
            qrSlideScanner.barcodeView.cameraSettings = s
            //barcodeScannerView.viewFinder.visibility = View.INVISIBLE
            qrSlideScanner.setStatusText(" ")
            //val formats = Arrays.asList(BarcodeFormat.)
            //qrSlideScanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
            //qrSlideScanner.decodeContinuous(callback)
        qrSlideScanner.decodeSingle(callback)
            qrSlideScanner.pauseAndWait()


    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            qrSlideScanner.pause()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //qrSlideScanner.visibility = View.GONE
            if (result.text == null) {
                return
            }
            lastText = result.text
            HandleQrResutl()
        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun HandleQrResutl(){
        when(qResult.handleQrResult(lastText.toString())){
            0 ->{}//No funciono
            1 -> mensaje("correcto") //formato correcto
            2 -> startUrlAlert(lastText.toString()) //url
            3 -> mensaje(lastText.toString(), "Contenido QR") //contenido sin formato
            4 -> mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")
            5 -> mensaje("QR expirado", "QR respuesta")
        }
    }

    private fun startUrlAlert(qrContents: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Desea usted abrir \"$qrContents\"?")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    Toast.makeText(this, "abriendo $qrContents", Toast.LENGTH_LONG).show()
                    val i = Intent(this, WebViewActivity::class.java)
                    i.putExtra("url", qrContents)
                    i.putExtra("titulo", "QR")
                    startActivityForResult(i, 0)
                }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    /**
     * ABRE UN NUEVO FRAGMENTO ENCIMA DEL ANTERIOR
     */
    private fun openFragment(fragment: Fragment) {
        fm.beginTransaction()
                //.setCustomAnimations(R.anim.slide_in_bottom, 0)
                .replace(R.id.container, fragment)
                //.add(R.id.container, fragment)

                //.addToBackStack(null)
                .commit()
    }

    /**
     * Preguntar al usuario si desea salir que pulse de nuevo al boton ATRAS
     */
     override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Presionar atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
        }
        else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mHandler.postDelayed(mRunnable, 1000)
    }

    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeCallbacks(mRunnable)
    }

    private fun mensaje(msg: String= "no especificado", ttl:String="titulo generico" ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg).setTitle(ttl)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()

        qrSlideScanner.resume()
    }

    override fun onPause() {
        super.onPause()

        qrSlideScanner.pause()
    }

    /*fun showNotification(title:String, content:String){

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, notification(title, content).build())

    }*/

    /*private fun notification(title:String, content:String){
        /*val intent = Intent(ct, InicioActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(ct, 0, intent, 0)*/
        val CHANNEL_ID = "idk"
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.settings)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle( NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }*/


}
