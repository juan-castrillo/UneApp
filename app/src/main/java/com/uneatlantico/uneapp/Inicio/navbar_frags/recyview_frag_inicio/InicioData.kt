package com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio

import android.content.Context
import com.uneatlantico.uneapp.db.estructuras_db.Noticia

//TODO conseguir informacion de la pagina de Uneatlantico sobre las noticias
/**
 *
 */
class InicioData{
    var listaNoticias= ArrayList<Noticia>()
    constructor(ct:Context){
        //val imagenNoticia = Picasso.with(ct).load("").resize(50,50).get()
        fillLista(ct)
    }

    fun parsePost(){

    }

    fun requestPost(){

    }

    fun fillLista(ct: Context){

        for (i in 1..3) {
            val url: String = "https://google.es"
            val titulo: String = "randomtarjeta"
            val contenido: String = "holasoyunatarjetaxd"
            //val imagenNoticia = Picasso.with(ct).load("https://noticias.uneatlantico.es/wp-content/uploads/2018/04/relatos_web-300x200.jpg").resize(50, 50).get()
            val urlImagen: String = "https://noticias.uneatlantico.es/wp-content/uploads/2018/04/relatos_web-300x200.jpg"
            listaNoticias.add(Noticia(url, titulo, contenido, imagenUrl =  urlImagen))
        }

    }

}