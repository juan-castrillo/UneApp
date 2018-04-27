package com.uneatlantico.uneapp.db

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.uneatlantico.uneapp.db.estructuras_db.Progreso
import com.uneatlantico.uneapp.db.estructuras_db.Registro
import org.jetbrains.anko.doAsync

class UneAppExecuter{
    var ct:Context
    constructor(ct: Context){
        this.ct = ct
    }

    /**
     * insertar usuario desde login
     */
    fun insertarUsuario(listaInsert: List<String>){
        doAsync {
            val db = UneAppDB(ct).readableDatabase
            val sql = "INSERT INTO usuario (idPersona ,nombre, email, photoUrl) VALUES ('${listaInsert[0]}', '${listaInsert[1]}', '${listaInsert[2]}', '${listaInsert[3]}')"
            Log.d("insertUsuario", sql)
            db.execSQL(sql)

            db.close()
        }
    }

    /**
     * devolver a clase login confirmacion de que existe un usuario
     * 1 existe usuario
     * 0 no existe usuario
     */
    fun devolverUsuario():Short {
        val db = UneAppDB(ct).readableDatabase
        var id:Short = 0
        try {
            val cursor = db.rawQuery("select _id from usuario", null)
            if (cursor.moveToFirst())
                id = cursor.getShort(cursor.getColumnIndex("_id"))
            Log.d("idparalogin", id.toString())
            cursor.close()
        }
        catch (e:Exception) {Log.d("idparaLoginnoDevuelto", e.message)}
        return id
    }
    companion object {
        fun recogerRegistros(ct: Context): ArrayList<Registro> {
            val registros = ArrayList<Registro>()
            doAsync {
                //Log.d("rutaDB", DB_PATH)
                val db = UneAppDB(ct).readableDatabase
                lateinit var cursor: Cursor
                try {
                    cursor = db.rawQuery("select e.nombreEvento idEvento,r.fecha fecha,r.enviado enviado,r.estado estado from registros r, eventos e where r.idEvento = e._id", null)
                } catch (e: Exception) {
                    Log.d("queryRecogerAll", e.message)
                }
                try {
                    //var id: Int
                    var Evento: String
                    var fecha: String
                    //var estado: Int
                    var enviado: Int
                    if (cursor.moveToFirst())
                        while (!cursor.isAfterLast) {
                            //id = cursor.getInt(cursor.getColumnIndex("_id"))
                            Evento = cursor.getString(cursor.getColumnIndex("idEvento"))
                            fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                            //estado = cursor.getInt(cursor.getColumnIndex("estado"))
                            enviado = cursor.getInt(cursor.getColumnIndex("enviado"))

                            registros.add(Registro(Evento = Evento, fecha = fecha, enviado = enviado))
                            cursor.moveToNext()
                        }
                } catch (x: Exception) {
                    Log.d("asignacionRecogerAll", x.message)
                }
                cursor.close()

            }
            return registros
        }

        fun estadoUltimo(ct: Context, idEvento: Int, fechahoy:String):Int{
            var estado: Int = 0
            //lateinit var cursor: Cursor
            val db = UneAppDB(ct).readableDatabase
            try {
                val cursor = db.rawQuery("select estado from registros WHERE idEvento = $idEvento AND fecha LIKE '%$fechahoy%' ORDER BY _id DESC LIMIT 1", null)

                if (cursor.moveToFirst())
                    while (!cursor.isAfterLast) {
                        estado = cursor.getString(cursor.getColumnIndex("estado")).toInt()
                        cursor.moveToNext() }
                Log.d("estadoultimoDB", estado.toString())
                cursor.close()
            }
            catch (e: Exception) {
                Log.d("error consiguiendo ultimo registro", e.message)}

            return estado
        }

        /**
         * Inserta al usuario si es que no hay ninguno
         */
        /*fun insertarUsuario(ct: Context){
            val db = UneAppDB(ct).readableDatabase
        }*/

        /**
         * devuelve un unico usuario
         */
        fun devolverUsuario(ct: Context):List<String> {
            var GsignIn:List<String> = emptyList()
            val db = UneAppDB(ct).readableDatabase
            //var idPersona:String
            var nombre:String
            var email:String
            var photoUrl:String

            try {
                val cursor = db.rawQuery("select * from usuario", null)

                    if (cursor.moveToFirst()) {
                        //idPersona = cursor.getString(cursor.getColumnIndex("idPersona"))
                        nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                        email = cursor.getString(cursor.getColumnIndex("email"))
                        photoUrl = cursor.getString(cursor.getColumnIndex("photoUrl"))
                        Log.d("usuarioString", nombre + email + photoUrl)
                        GsignIn = listOf(nombre, email, photoUrl)
                    }

                cursor.close()
            }
            catch (e: Exception) {
                Log.d("usuarioNoEncontradoEnDB", e.message)}

            return GsignIn
        }

        fun getIdPersona(ct: Context):String{
            var idPersona = ""
            val db = UneAppDB(ct).readableDatabase
            try {
                val cursor = db.rawQuery("select idPersona from usuario", null)
                if (cursor.moveToFirst())
                    idPersona = cursor.getString(cursor.getColumnIndex("idPersona"))
            }
            catch (e:Exception){

            }
            return idPersona
        }

        fun saveRegistroInDB(listaInsert: List<String>, ct:Context){
            doAsync {
                val db = UneAppDB(ct)
                val writableDB = db.writableDatabase
                val sql = "INSERT INTO registros (idEvento, fecha, enviado, estado) VALUES ('${listaInsert[0]}', '${listaInsert[1]}', '${listaInsert[2]}', '${listaInsert[3]}')"
                Log.d("insertRegistro", sql)
                writableDB.execSQL(sql)

                writableDB.close()
            }
        }

        fun insertarProgreso(ct: Context, progreso: Progreso){
            doAsync {
                val db = UneAppDB(ct)
                val writableDB = db.writableDatabase
                val sql = "INSERT INTO progreso_asistencia (idEvento, horasAlumno, horasEvento) VALUES ('${progreso.idEvento}', '${progreso.horasAlumno}', '${progreso.horasEventoTotales}')"
                Log.d("insertProgreso", sql)
                writableDB.execSQL(sql)

                writableDB.close()
            }
        }

        fun updateProgreso(ct: Context, progreso: Progreso) {
            doAsync {
                val db = UneAppDB(ct)
                val writableDB = db.writableDatabase
                val sql = "UPDATE progreso_asistencia set horasAlumno = '${progreso.horasAlumno}', horasEvento = '${progreso.horasEventoTotales}' where idEvento = '${progreso.idEvento}'"
                Log.d("insertProgreso", sql)
                writableDB.execSQL(sql)

                writableDB.close()
            }
        }

        fun devolverProgreso(ct: Context):ArrayList<Progreso>{
            val progresos = ArrayList<Progreso>()
            doAsync {

                var evento: String
                var horasAlumno: Float
                var horasEvento: Float
                try {
                    val db = UneAppDB(ct).readableDatabase
                    val cursor = db.rawQuery("select e.nombreEvento idEvento,r.horasAlumno horasAlumno,r.horasEvento horasEvento from progreso_asistencia r, eventos e where r.idEvento = e._id", null)

                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast) {
                            //idPersona = cursor.getString(cursor.getColumnIndex("idPersona"))
                            evento = cursor.getString(cursor.getColumnIndex("idEvento"))
                            horasAlumno = cursor.getFloat(cursor.getColumnIndex("horasAlumno"))
                            horasEvento = cursor.getFloat(cursor.getColumnIndex("horasEvento"))
                            Log.d("progresoString", evento + horasAlumno + horasEvento)
                            progresos.add(Progreso(Evento = evento, horasAlumno = horasAlumno, horasEventoTotales = horasEvento))
                            cursor.moveToNext()
                        }
                    }
                    cursor.close()
                }
                catch (e: Exception) {
                    Log.d("progresosNoTraidos", e.message)}
            }
            return progresos
        }

        fun checkProgresoExiste(idProgreso:Int, ct: Context):Short{
            val db = UneAppDB(ct).readableDatabase
            var count:Short = 0
            try {
                val sql = "select COUNT(idEvento) o from progreso_asistencia WHERE idEvento = '$idProgreso'"
                val cursor = db.rawQuery(sql, null)
                if (cursor.moveToFirst())
                    count = cursor.getShort(cursor.getColumnIndex("o"))
                Log.d("numerosqlcheckprogreso", sql)
                Log.d("numeroProgresoigual", count.toString())
                cursor.close()
            }
            catch (e:Exception) {}
            return count
        }

        //TODO mejorar el where y areglar parametros
        fun updateRegistro(updateList:List<String>) {
            doAsync {
                val sql = "UPDATE registros set enviado = 1 WHERE idEvento = ${updateList[0]} AND fecha = ${updateList[1]}"
            }
        }
    }
}