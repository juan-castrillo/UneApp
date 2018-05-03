package com.uneatlantico.uneapp.Inicio

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
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
import kotlin.reflect.KClass
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.*
import kotlinx.android.synthetic.main.activity_inicio.view.*


class InicioActivity : AppCompatActivity() {

    //Menu lateral de hamburguesa
    //private lateinit var googleAccount: GoogleSignInAccount
    private lateinit var googleAccount: List<String>
    private lateinit var mName: TextView
    private lateinit var mImage: ImageView
    private lateinit var mMail: TextView
    private lateinit var headerView: View
    private lateinit var menuImageView: ImageView
    //TODO eliminar estas variables
    //fragmentos para la barra de navegacion inferior
    private var inicioFragment = InicioFragment.newInstance()
    /*private var campusFragment = CampusFragment.newInstance()
    private var qrScannerFragment = QrScannerFragment()
    private var horarioFragment = HorarioFragment.newInstance()*/
    private val fm = supportFragmentManager
    private val menuFragment = MenuFragment.newInstance()
    private lateinit var fullmenu:FrameLayout
    //variables para el menu hamburguesa lateral
    private lateinit var toolbar: Toolbar
    //variables doble pulsacion para salir
    private var doubleBackToExitPressedOnce = false
    private val mHandler = Handler()
    private lateinit var colorOverlay:LinearLayout
    private lateinit var dissmissmenu:FrameLayout
    /**
     * https://github.com/umano/AndroidSlidingUpPanel
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        //googleAccount = intent.extras.getParcelable("account")
        googleAccount = devolverUsuario(this)
        openFragment(inicioFragment)

        //Menu hamburguesa Ya implementado completamente https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = " "
        fullmenu = findViewById(R.id.fullmenu)
        colorOverlay = findViewById(R.id.greycontainer)
        //colorOverlay.dissmiss_menu.setOnClickListener { closeMenu() }
        dissmissmenu = findViewById(R.id.dissmiss_menu)
        dissmissmenu.setOnClickListener {
            closeMenu()
        }
        menuImageView = toolbar.findViewById(R.id.menuImage)
        menuImageView.setOnClickListener {
            
            dropMenu()
            colorOverlay.alpha = 0.5F
        }
        //All sobre la barra de navegacion inferior
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        BottomNavigationViewComplements.removeShiftMode(bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     * desplegates menu yes
     */
    private fun dropMenu() {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.menu_down, 0)
                .replace(R.id.containeador,menuFragment, "menu")
                //.add(R.id.container2, MenuFragment.newInstance())
                //.addToBackStack(null)
                .commit()
        Log.d("droppedmenu", "yes")
    }

    private fun ham_Launch(ina: KClass<*>, ham_option_title:String) {
        val i = Intent(this, ina.java)
        i.putExtra("title", ham_option_title)
        //finish()  //Kill the activity from which you will go to next activity
        startActivityForResult(i, 0)
    }

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
    public override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        if(fm.findFragmentByTag("menu") == null) {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Presionar atrás de nuevo para salir", Toast.LENGTH_SHORT).show()
        }
        else closeMenu()
        mHandler.postDelayed(mRunnable, 1000)
    }

    private fun closeMenu() {

        fm.beginTransaction().setCustomAnimations(0,R.anim.menu_up).replace(R.id.containeador, Fragment()).commit()
        colorOverlay.alpha = 0F
        //fm.beginTransaction().setCustomAnimations(R.anim.menu_up, 0).remove(menuFragment).commit()
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

}
