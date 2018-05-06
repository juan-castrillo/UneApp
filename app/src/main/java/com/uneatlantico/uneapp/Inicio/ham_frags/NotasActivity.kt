package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle

import android.support.v7.app.AppCompatActivity

import com.uneatlantico.uneapp.R



class NotasActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        /*val barcodeCapture = supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture
        barcodeCapture.setRetrieval(this)*/

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    /*override fun onRetrieved(barcode: Barcode) {
        Log.d( "Barcode read: " , barcode.displayValue)
        runOnUiThread {
            val builder = AlertDialog.Builder(this)
                    .setTitle("code retrieved")
                    .setMessage(barcode.displayValue)
            builder.show()
        }


    }

    // for multiple callback
    override fun onRetrievedMultiple(closetToClick: Barcode, barcodeGraphics: List<BarcodeGraphic>) {
        runOnUiThread {
            var message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                    "codes in frame include : \n"
            for (index in barcodeGraphics.indices) {
                val barcode = barcodeGraphics[index].barcode
                message += (index + 1).toString() + ". " + barcode.displayValue + "\n"
            }
            val builder = AlertDialog.Builder(this)
                    .setTitle("code retrieved")
                    .setMessage(message)
            builder.show()
        }

    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>) {
        // when image is scanned and processed
    }

    override fun onRetrievedFailed(reason: String) {
        // in case of failure
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }*/
}
