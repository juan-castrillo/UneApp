package com.uneatlantico.uneapp.db.estructuras_db

import java.util.*

class Registro(var map: MutableMap<String, Any?>) {
    var _id: Int by map
    var Evento: String by map
    var fecha: String by map
    var estado: Int by map
    var enviado: Int by map
    var idEvento:Int by map

    constructor(id:Int = 1,idEvento:Int=0 ,Evento:String="", fecha:String="", estado:Int=0, enviado:Int=0):this(HashMap()){
        this._id = id
        this.idEvento = idEvento
        this.Evento = Evento
        this.fecha = fecha
        this.estado = estado
        this.enviado = enviado
    }
}