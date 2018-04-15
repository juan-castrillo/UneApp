package com.uneatlantico.uneapp.Inicio.navbar_frags

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.uneatlantico.uneapp.R


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val integrator = IntentIntegrator.forSupportFragment(this)

        integrator.setPrompt(" ")


        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.initiateScan()
    }

    override fun onClick(v: View) {
        IntentIntegrator.forSupportFragment(this).setPrompt("Scan a barcode or QRCode").initiateScan()
        /*val integrator = IntentIntegrator.forSupportFragment(this)

        integrator.setPrompt(" ")


        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.initiateScan()*/
    }
    /**
     * Devuelve el resultado del escaneo de qr
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result !== null) {
            if (resultCode == Activity.RESULT_OK) {
                if (result.contents == null)
                    Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this.context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
            /*val getMainScreen = Intent(this, InicioActivity::class.java)//pentru test, de sters
        startActivity(getMainScreen)*/

    }
        /*catch (x: Exception){
            val getMainScreen = Intent(this.context, InicioActivity::class.java)//pentru test, de sters
            startActivity(getMainScreen)
        }*/



        /*btn.setOnClickListener(View.OnClickListener{
            val integrator = IntentIntegrator.forSupportFragment(this)

            integrator.setPrompt(" ")


            integrator.setCameraId(0)  // Use a specific camera of the device
            integrator.initiateScan()
        })*/




    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }
}// Required empty public constructor
