package com.uneatlantico.uneapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Fragmento del Horario
 */
class HorarioFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_horario, container, false)

    companion object {
        fun newInstance(): HorarioFragment = HorarioFragment()
    }
}// Required empty public constructor
