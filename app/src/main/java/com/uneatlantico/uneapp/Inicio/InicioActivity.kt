package com.uneatlantico.uneapp.Inicio

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.uneatlantico.uneapp.BottomNavigationViewComplements
import com.uneatlantico.uneapp.Inicio.ham_frags.ExtraFragment
import com.uneatlantico.uneapp.Inicio.ham_frags.NotasFragment
import com.uneatlantico.uneapp.Inicio.ham_frags.RegistroAsistenciaFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.CampusFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.HorarioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.InicioFragment
import com.uneatlantico.uneapp.Inicio.navbar_frags.QrScannerFragment
import com.uneatlantico.uneapp.R
import kotlinx.android.synthetic.main.activity_inicio.*


class InicioActivity : AppCompatActivity() {

    //fragmentos para la barra de navegacion inferior
    private var inicioFragment = InicioFragment.newInstance()
    private var campusFragment = CampusFragment.newInstance()
    private var qrScannerFragment = QrScannerFragment.newInstance()
    private var horarioFragment = HorarioFragment.newInstance()
    private val fm = supportFragmentManager

    //variables para el menu hamburguesa lateral
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var nvDrawer: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val account: Bundle = intent.extras
        if(account != null) {
            val accountation = account.get("account")
        }
        openFragment(inicioFragment)

        //https://github.com/journeyapps/zxing-android-embedded

        //TODO implementar menu hamburguesa --> https://github.com/mikepenz/MaterialDrawer
        //Ya implementada completamente https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        nvDrawer = findViewById<NavigationView>(R.id.nvView)
        setupDrawerContent(nvDrawer)
        drawerToggle = setupDrawerToggle()

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.addDrawerListener(drawerToggle);


        //All sobre la barra de navegacion inferior
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        BottomNavigationViewComplements.removeShiftMode(bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

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

        when (menuItem.itemId) {
            R.id.ham_notas -> openFragment(NotasFragment.newInstance())
            R.id.ham_registro_asistencias -> openFragment(RegistroAsistenciaFragment.newInstance())
            R.id.ham_extra -> openFragment(ExtraFragment.newInstance())
            else -> openFragment(NotasFragment.newInstance())
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        title = menuItem.title
        // Close the navigation drawer
        mDrawerLayout.closeDrawers()
    }

    /**
     *  cambiar entre fragmentos mediante la barra de navegacion inferior
     */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                inicioFragment = InicioFragment.newInstance()
                openFragment(inicioFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_campus -> {

                //campusFragment = CampusFragment.newInstance()
                openFragment(campusFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                qrScannerFragment = QrScannerFragment.newInstance()
                openFragment(qrScannerFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_horario -> {
                horarioFragment = HorarioFragment.newInstance()
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
        val transaction = fm.beginTransaction()
        //hideAllFragments()
        transaction.replace(R.id.container, fragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun hideAllFragments(){
        val transaction2 = fm.beginTransaction()
        transaction2.hide(inicioFragment)
        transaction2.hide(campusFragment)
        transaction2.hide(qrScannerFragment)
        transaction2.hide(horarioFragment)
        transaction2.commit()
    }

}
