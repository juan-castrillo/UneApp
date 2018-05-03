package com.uneatlantico.uneapp.Inicio

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverUsuario
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MenuFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MenuFragment : Fragment() {
    private lateinit var menuGoogleUserName:TextView
    private lateinit var menuGoogleUserEmail:TextView
    private lateinit var menuGoogleUserImage:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_menu, container, false)

        menuGoogleUserName = v.findViewById(R.id.menuGoogleUserName)
        menuGoogleUserEmail = v.findViewById(R.id.menuGoogleUserEmail)
        menuGoogleUserImage = v.findViewById(R.id.menuGoogleUserImage)
        initMenu()
        return v
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
