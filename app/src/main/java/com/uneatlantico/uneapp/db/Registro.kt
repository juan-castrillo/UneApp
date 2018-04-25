package com.uneatlantico.uneapp.db

import java.util.*

class Registro(var map: MutableMap<String, Any?>) {
    var _id: Long by map
    var Evento: String by map
    var fecha: String by map
    var estado: Int by map
    var enviado: Int by map

    constructor(Evento:String, fecha: String, estado:Int, enviado:Int):this(HashMap()){
        this.Evento = Evento
        this.fecha = fecha
        this.estado = estado
        this.enviado = enviado
    }
}