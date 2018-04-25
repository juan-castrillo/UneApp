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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.uneatlantico.uneapp.BottomNavigationViewComplements
import com.uneatlantico.uneapp.Inicio.ham_frags.ExtraActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.NotasActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.RegistroAsistenciaActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.SettingsActivity
import com.uneatlantico.uneapp.Inicio.navbar_frags.CampusFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.HorarioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.InicioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.QrScannerFragment
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverUsuario
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlin.reflect.KClass


class InicioActivity : AppCompatActivity() {

    //Cuenta de google
    //private lateinit var googleAccount: GoogleSignInAccount
    private lateinit var googleAccount: List<String>
    private lateinit var mName: TextView
    private lateinit var mImage: ImageView
    private lateinit var mMail: TextView
    private lateinit var headerView: View

    //TODO eliminar estas variables
    //fragmentos para la barra de navegacion inferior
    private var inicioFragment = InicioFragment.newInstance()
    private var campusFragment = CampusFragment.newInstance()
    private var qrScannerFragment = QrScannerFragment()
    private var horarioFragment = HorarioFragment.newInstance()
    private val fm = supportFragmentManager

    //variables para el menu hamburguesa lateral
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var nvDrawer: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle

    //variables doble pulsacion para salir
    private var doubleBackToExitPressedOnce = false
    private val mHandler = Handler()

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        //googleAccount = intent.extras.getParcelable("account")
        googleAccount = devolverUsuario(this)
        openFragment(inicioFragment)

        //Ya implementada completamente https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        nvDrawer = findViewById(R.id.nvView)
        setupDrawerContent(nvDrawer)

        headerView = nvDrawer.getHeaderView(0)

        headerData()
        drawerToggle = setupDrawerToggle()
        mDrawerLayout.addDrawerListener(drawerToggle)

        //All sobre la barra de navegacion inferior
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        BottomNavigationViewComplements.removeShiftMode(bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun headerData() {
        mName = headerView .findViewById(R.id.headerUserName)

        mMail = headerView .findViewById(R.id.headerUserEmail)

        mImage = headerView .findViewById(R.id.headerUserImage)

        try{
            mName.text = googleAccount[0]
            mMail.text = googleAccount[1]
            Picasso.with(this).load(googleAccount[2]).into(mImage)
        }
        catch (e:Exception){
            Log.d("googleAccountVacio", e.message)
        }
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item))
                return true
        return super.onOptionsItemSelected(item)
    }

    /**
     *
     */
    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    /**
     * cambiar entre fragmentos mediante el menu de hamburguesa
     */
    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var hamActivitie: KClass<*>
        when (menuItem.itemId) {
            R.id.ham_notas -> hamActivitie = NotasActivity::class//openFragment(NotasFragment.newInstance())
            R.id.ham_registro_asistencias -> hamActivitie = RegistroAsistenciaActivity::class //openFragment(RegistroAsistenciaFragment.newInstance())
            R.id.ham_extra -> hamActivitie =ExtraActivity::class //openFragment(ExtraFragment.newInstance())
            R.id.ham_settings ->hamActivitie =SettingsActivity::class //openFragment(SettingsFragment.newInstance())
            else -> hamActivitie = SettingsActivity::class //openFragment(NotasFragment.newInstance())
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        //title = menuItem.title
        // Close the navigation drawer
        mDrawerLayout.closeDrawers()
        ham_Launch(hamActivitie, menuItem.title.toString())
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
                openFragment(inicioFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_campus -> {

                //campusFragment = CampusFragment.newInstance()
                openFragment(campusFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                //qrScannerFragment = QrScannerFragment.newInstance()
                openFragment(qrScannerFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_horario -> {
                //horarioFragment = HorarioFragment.newInstance()
                openFragment(horarioFragment)
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
                .replace(R.id.container, fragment)
                //.addToBackStack(null)
                .commit()
    }

    private fun hideAllFragments(){
        val transaction2 = fm.beginTransaction()
        transaction2.hide(inicioFragment)
        transaction2.hide(campusFragment)
        transaction2.hide(qrScannerFragment)
        transaction2.hide(horarioFragment)
        transaction2.commit()
    }

    /**
     * Preguntar al usuario si desea salir que pulse de nuevo al boton ATRAS
     */
    public override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Presionar atrás de nuevo para salir", Toast.LENGTH_SHORT).show()

        mHandler.postDelayed(mRunnable, 2000)
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
