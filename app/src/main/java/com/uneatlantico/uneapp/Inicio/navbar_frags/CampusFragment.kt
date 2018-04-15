package com.uneatlantico.uneapp.Inicio.navbar_frags

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.uneatlantico.uneapp.R


/**
 * Fragmento del campus Uneatlantico
 * muestra la pagina web movil del sitio
 */
class CampusFragment : Fragment() {

    //override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            //inflater.inflate(R.layout.fragment_campus, container, false)
    var mWebView: WebView? = null

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater!!.inflate(R.layout.fragment_campus, container, false)
        mWebView = v.findViewById<WebView>(R.id.webview) as WebView
        mWebView!!.loadUrl("https://www.uneatlantico.es/alumnos/uneatlantico-virtual")

        // Enable Javascript
        val webSettings = mWebView!!.getSettings()
        webSettings.setJavaScriptEnabled(true)

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView!!.setWebViewClient(WebViewClient())


        return v
    }
    companion object {
        fun newInstance(): CampusFragment = CampusFragment()
    }
}// Required empty public constructor
