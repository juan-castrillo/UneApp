package com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.uneatlantico.uneapp.R

class InicioWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_web_view)
        //val url:String = intent.extras.getParcelable("url")
        val url = intent.extras.getString("url")
        Log.d("url", url)

        val mWebView = findViewById<WebView>(R.id.inicioWebView)
        mWebView.loadUrl(url)

        // Enable Javascript
        val webSettings = mWebView.getSettings()
        webSettings.javaScriptEnabled = false

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.webViewClient = WebViewClient()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.extras.getString("titulo")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
