package com.uneatlantico.uneapp.db

import com.orm.SugarRecord

class UneAppDB: SugarRecord {
    var idEvento: Long
    var fecha: String

    constructor(idEvento: Long, fecha: String){
        this.idEvento = idEvento
        this.fecha = fecha
    }
}