package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uneatlantico.uneapp.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NotasFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NotasFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NotasFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_notas, container, false)

    companion object {
        fun newInstance(): NotasFragment = NotasFragment()
    }
}
