package com.uneatlantico.uneapp.Inicio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import com.uneatlantico.uneapp.BottomNavigationViewComplements
import com.uneatlantico.uneapp.Inicio.ham_frags.*
import com.uneatlantico.uneapp.Inicio.navbar_frags.CampusFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.HorarioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.InicioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.QrScannerFragment
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverUsuario
import kotlinx.android.synthetic.main.activity_inicio.*
import android.widget.*
import com.flipboard.bottomsheet.BottomSheetLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.uneatlantico.uneapp.QResult
import com.uneatlantico.uneapp.WebViewActivity
import kotlinx.android.synthetic.main.activity_inicio.view.*
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
        openFragment(inicioFragment)

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
                        if(s.requestedCameraId !=0) {
                            qrScanner(0)
                        }
                        //qrSlideScanner.decodeSingle(callback)
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
                if(slideOffset < 0.5)
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
            bottomSheetFragment.show(fm, bottomSheetFragment.tag)
        }

        //All sobre la barra de navegacion inferior
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        BottomNavigationViewComplements.removeShiftMode(bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

    private fun qrScanner(cameraId:Int = 3) {

        s.isExposureEnabled = false
        s.isMeteringEnabled = false

        //s.isScanInverted = false
        s.requestedCameraId = cameraId
        qrSlideScanner = findViewById(R.id.qr_slide_scanner)
        qrSlideScanner.barcodeView.cameraSettings = s
        //barcodeScannerView.viewFinder.visibility = View.INVISIBLE
        qrSlideScanner.setStatusText(" ")
        val formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        qrSlideScanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        qrSlideScanner.decodeContinuous(callback)
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
            when(qResult.handleQrResult(lastText.toString())){
                0 ->{}//No funciono
                1->{mensaje("correcto")} //formato correcto
                2->{
                    startUrlAlert(lastText.toString())
                } //url
                3->mensaje(lastText.toString(), "Contenido QR") //contenido sin formato
                4 -> mensaje("Debes estar conectado a la red de la universidad", "Alerta Escaner QR")

                5 -> mensaje("QR expirado", "QR respuesta")
            }

        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun startUrlAlert(qrContents: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Desea usted abrir \"$qrContents\"?")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    Toast.makeText(this, "abriendo $qrContents", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, WebViewActivity::class.java)
                    i.putExtra("url", qrContents)
                    i.putExtra("titulo", "QR")
                    startActivityForResult(i, 0)
                }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    /*private fun ham_Launch(ina: KClass<*>, ham_option_title:String) {
        val i = Intent(this, ina.java)
        i.putExtra("title", ham_option_title)
        //finish()  //Kill the activity from which you will go to next activity
        startActivityForResult(i, 0)
    }*/

    /**
     *  cambiar entre fragmentos mediante la barra de navegacion inferior
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                //val inicioFragment = InicioFragment.newInstance()
                openFragment(InicioFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_campus -> {

                //campusFragment = CampusFragment.newInstance()
                openFragment(CampusFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                //qrScannerFragment = QrScannerFragment.newInstance()
                openFragment(QrScannerFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_horario -> {
                //fm.beginTransaction().setCustomAnimations(R.anim.menu_down, 0).replace(R.id.containeador,menuFragment).commit()
                //fm.beginTransaction().setCustomAnimations(R.anim.menu_down, 0).replace(R.id.containeador, HorarioFragment.newInstance()).commit()
                //horarioFragment = HorarioFragment.newInstance()
                openFragment(HorarioFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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

    /*private fun closeMenu() {

        fm.beginTransaction().setCustomAnimations(0,R.anim.menu_up).replace(R.id.containeador, Fragment()).commit()
        colorOverlay.alpha = 0F
        //fm.beginTransaction().setCustomAnimations(R.anim.menu_up, 0).remove(menuFragment).commit()
    }*/

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

}
