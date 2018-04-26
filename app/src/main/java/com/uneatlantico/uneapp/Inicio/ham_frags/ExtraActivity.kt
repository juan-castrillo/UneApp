package com.uneatlantico.uneapp.Inicio.ham_frags

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import com.uneatlantico.uneapp.R
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.google.zxing.BarcodeFormat
import java.util.Arrays.asList
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeCallback
import java.util.*


/**
 * https://github.com/akexorcist/Android-RoundCornerProgressBar
 */
class ExtraActivity : Activity() {
    //private val TAG = ExtraActivity::class.java.simpleName
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var beepManager: BeepManager
    private lateinit var lastText: String

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            lastText = result.text
        }
            //barcodeView.setStatusText(result.text)

            //beepManager.playBeepSoundAndVibrate()

            //Added preview of scanned barcode
            //val imageView = findViewById(R.id.qrwindow) as ImageView
            //imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))


        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_extra)

        barcodeView = findViewById(R.id.qrwindow) as DecoratedBarcodeView
        val formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.decodeContinuous(callback)

        //beepManager = BeepManager(this)
    }

    override fun onResume() {
        super.onResume()

        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView.pause()
    }

    fun pause(view: View) {
        barcodeView.pause()
    }

    fun resume(view: View) {
        barcodeView.resume()
    }

    fun triggerScan(view: View) {
        barcodeView.decodeSingle(callback)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }*/
}
