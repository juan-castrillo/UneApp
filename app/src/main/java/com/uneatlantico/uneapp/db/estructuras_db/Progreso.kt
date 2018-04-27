package com.uneatlantico.uneapp.db.estructuras_db

import java.util.HashMap

class Progreso(var map: MutableMap<String, Any?>) {
    var _id: Int by map
    var idEvento: Int by map
    var Evento: String by map
    var horasAlumno: Float by map
    var horasEventoTotales: Float by map

    constructor(idEvento:Int = 0, Evento:String="", horasAlumno:Float, horasEventoTotales:Float):this(HashMap()){
        this.idEvento = idEvento
        this.Evento = Evento
        this.horasAlumno = horasAlumno
        this.horasEventoTotales = horasEventoTotales
    }
}