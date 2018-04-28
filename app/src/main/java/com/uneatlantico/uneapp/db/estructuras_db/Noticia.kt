package com.uneatlantico.uneapp.db.estructuras_db

import java.util.*

class Noticia(var map: MutableMap<String, Any?>) {
    var url: String by map
    var titulo: String by map
    var contenido: String by map
    //var imagen:Bitmap? by map
    var imagenUrl: String by map
    var fecha:String by map

    constructor(url:String, titulo:String, contenido:String, imagenUrl:String = "", fecha:String = ""):this(HashMap()){
        this.url = url
        this.titulo = titulo
        this.contenido = contenido
        //this.imagen = imagen
        this.imagenUrl = imagenUrl
        this.fecha = fecha
    }
}