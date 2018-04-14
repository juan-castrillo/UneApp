package com.uneatlantico.uneapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.zxing.integration.android.IntentIntegrator


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //IntentIntegrator.forSupportFragment(this).setPrompt("Scan a barcode or QRCode").initiateScan()
        val button = inflater.inflate(R.layout.fragment_qr_scanner, container, false).findViewById(R.id.button) as Button
        button.setOnClickListener(this)
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false)
    }


    override fun onClick(v: View) {
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan a barcode or QRCode").initiateScan()
        /*val integrator = IntentIntegrator.forSupportFragment(this)

        integrator.setPrompt(" ")


        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.initiateScan()*/
    }

   /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*btn.setOnClickListener(View.OnClickListener{
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setPrompt(" ")


            integrator.setCameraId(0)  // Use a specific camera of the device
            integrator.initiateScan()
        })*/
    }*/



    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }
}// Required empty public constructor
