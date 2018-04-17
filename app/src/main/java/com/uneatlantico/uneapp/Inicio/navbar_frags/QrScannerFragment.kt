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
import com.uneatlantico.uneapp.db.RegistrosDataBase
import org.jetbrains.anko.db.insert


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrScannerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScannerFragment : Fragment(), View.OnClickListener {
    val mibonitoFragmento: QrScannerFragment = this
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        val b: Button = v.findViewById(R.id.button) as Button
        b.setOnClickListener(this)
        return v
    }

    //TODO comprobar si el usuario está en la red de la universidad antes de permitir abrir el scanner
    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> {
                Toast.makeText(this.context, "has pulsado mi boton", Toast.LENGTH_SHORT)
                val integrator = IntentIntegrator.forSupportFragment(this)
                integrator.setPrompt(" ")
                integrator.setCameraId(0)  // Use a specific camera of the device
                integrator.initiateScan()
            }
            else ->Toast.makeText(this.context, "has pulsado algo raro", Toast.LENGTH_SHORT)
        }


    }
    /**
     * Devuelve el resultado del escaneo de qr
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result !== null) {
            if (resultCode == Activity.RESULT_OK) {
                if (result.contents == null)
                    Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(this.context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                    handleQrResult(result.contents)
                }
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Formateo el resultado de la lectura de qr
     * Compruebo si el formato es el adecuado
     */
    private fun handleQrResult(qrContents: String) {
        lateinit var idEvento: String
        lateinit var fecha: String
        try {
            val partes: List<String> = qrContents.split('_')
            val iteratorPartes = partes.iterator()
            while (iteratorPartes.hasNext()) {
                idEvento = iteratorPartes.next()
                fecha = iteratorPartes.next()
            }
        }
        catch (z: Exception){
            Toast.makeText(this.context, "no compatible", Toast.LENGTH_SHORT)//TODO hacer algo cuando el qr no cumpla los parámetros adecuados
        }

        insertarRegistroQr(idEvento, fecha)
    }

    /**
     * Inserto los datos del qr en base de datos
     */
    private fun insertarRegistroQr(idEvento: String, fecha: String) {
        val db = RegistrosDataBase(this.context)
        try {
            db.use{
                insert(
                    "Registros", "idEvento" to idEvento, "fecha" to fecha
                )
            }
        }
        catch (xc: Exception){
            Toast.makeText(this.context, "no insertado", Toast.LENGTH_SHORT)
        }
    }

    companion object {
        fun newInstance(): QrScannerFragment = QrScannerFragment()
    }
}// Required empty public constructor
