package com.uneatlantico.uneapp.db

import java.util.HashMap

class Registro(var map: MutableMap<String, Any?>) {
    var _id: Long by map
    var idEvento: Long by map
    var fecha: String by map

    constructor(idEvento:Long, fecha: String):this(HashMap()){
        this.idEvento = idEvento
        this.fecha = fecha
    }
}