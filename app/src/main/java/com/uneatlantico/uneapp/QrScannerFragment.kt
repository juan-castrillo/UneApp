package com.uneatlantico.uneapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_qr_scanner, container, false)



    // TODO: Rename method, update argument and hook method into UI event

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener(View.OnClickListener { })
    }*/


    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }
}// Required empty public constructor
