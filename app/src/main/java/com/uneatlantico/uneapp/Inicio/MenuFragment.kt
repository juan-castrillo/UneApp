package com.uneatlantico.uneapp.Inicio

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.flipboard.bottomsheet.commons.BottomSheetFragment
import com.squareup.picasso.Picasso

import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverUsuario
import org.w3c.dom.Text
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.MenuItem
import android.widget.LinearLayout
import com.uneatlantico.uneapp.Inicio.ham_frags.ExtraActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.NotasActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.RegistroAsistenciaActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.SettingsActivity
import kotlin.reflect.KClass


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MenuFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MenuFragment : BottomSheetDialogFragment() {
    private lateinit var menuGoogleUserName:TextView
    private lateinit var menuGoogleUserEmail:TextView
    private lateinit var menuGoogleUserImage:ImageView
    private lateinit var asistenciaPress:LinearLayout
    private lateinit var notasPress:LinearLayout
    //private lateinit var campusPress:LinearLayout
    //private lateinit var HorarioPress:LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        menuGoogleUserName = v.findViewById(R.id.menuGoogleUserName)
        menuGoogleUserEmail = v.findViewById(R.id.menuGoogleUserEmail)
        menuGoogleUserImage = v.findViewById(R.id.menuGoogleUserImage)
        asistenciaPress = v.findViewById(R.id.Asistencia)
        asistenciaPress.setOnClickListener{
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit();
            Launch(ExtraActivity::class, "Asistencias")
        }
        notasPress = v.findViewById(R.id.Notas)
        notasPress.setOnClickListener {
            activity!!.onBackPressed()
            Launch(NotasActivity::class, "Notas")
        }
        initMenu()
        return v
    }


    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var hamActivitie: KClass<*>
        when (menuItem.itemId) {
            R.id.ham_notas -> hamActivitie = NotasActivity::class//openFragment(NotasFragment.newInstance())
            R.id.ham_registro_asistencias -> hamActivitie = RegistroAsistenciaActivity::class //openFragment(RegistroAsistenciaFragment.newInstance())
            R.id.ham_extra -> hamActivitie = ExtraActivity::class //openFragment(ExtraFragment.newInstance())
            R.id.ham_settings ->hamActivitie = SettingsActivity::class //openFragment(SettingsFragment.newInstance())
            else -> hamActivitie = SettingsActivity::class //openFragment(NotasFragment.newInstance())
        }
        //Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        //title = menuItem.title
        // Close the navigation drawer

        Launch(hamActivitie, menuItem.title.toString())
    }

    private fun Launch(ina: KClass<*>, ham_option_title:String) {
        val i = Intent(this.context, ina.java)
        i.putExtra("title", ham_option_title)
        //finish()  //Kill the activity from which you will go to next activity
        startActivityForResult(i, 0)
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog

        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = 1000
        }
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight

            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun initMenu() {
        val usuario = devolverUsuario(this.context!!)
        menuGoogleUserName.text = usuario[0] //nombre
        menuGoogleUserEmail.text = usuario[1] //mail
        Picasso.with(this.context).load(usuario[2]).into(menuGoogleUserImage)

    }

    companion object {
        fun newInstance(): MenuFragment = MenuFragment()
    }
}
