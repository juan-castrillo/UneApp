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

        /**
         * devuelve todos los registros de todas las materias
         * para mostrar en registroAsistenciaActivity
         */
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

        /**
         * Devuelve el ultimo estado de una materia concreta para conocer
         * que estado se debe insertar en el siguiente registro
         */
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
         * devuelve un unico usuario de google con todos sus datos
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

        /**
         * devuelve el id de la persona
         */
        fun getIdPersona(ct: Context):String{
            var idPersona = ""
            val db = UneAppDB(ct).readableDatabase
            try {
                val cursor = db.rawQuery("select idPersona from usuario", null)
                if (cursor.moveToFirst())
                    idPersona = cursor.getString(cursor.getColumnIndex("idPersona"))
                cursor.close()
            }
            catch (e:Exception){

            }
            return idPersona
        }

        /**
         * Inserta un registro nuevo
         */
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

        /**
         * Consigo el registro segun el idEvento y la fecha
         */
        fun getRegistro(idEvent: Int, fech:String, ct: Context):Registro{
             var registro: Registro = Registro()
            doAsync {
                val db = UneAppDB(ct).readableDatabase
                val _id:Int
                val idEvento: Int
                val fecha:String
                val enviado:Int
                val estado:Int
                try {
                    val cursor = db.rawQuery("select * from registros where idEvento = $idEvent AND fecha = $fech", null)
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(cursor.getColumnIndex("_id"))
                        idEvento = cursor.getInt(cursor.getColumnIndex("idEvento"))
                        fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                        estado = cursor.getInt(cursor.getColumnIndex("estado"))
                        enviado = cursor.getInt(cursor.getColumnIndex("enviado"))
                        registro = Registro(idEvento = idEvento, fecha = fecha, enviado = enviado, estado = estado, _id= _id)
                    }
                    cursor.close()
                }
                catch (e: Exception){Log.d("idEventoNoEncontrado", e.message)}
            }
            return registro
        }

        /**
         * Cambio el campo enviado de un registro concreto al ser mandado al WS
         */
        fun updateRegistro(updateList:List<String>) {
            doAsync {
                val sql = "UPDATE registros set enviado = 1 WHERE idEvento = ${updateList[0]} AND fecha = ${updateList[1]}"
            }
        }

        /**
         * Inserta un progreso nuevo
         */
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

        /**
         * actualiza las horas de un progreso ya existente
         */
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

        /**
         * devuelve todos los progresos completos con nombre de evento
         */
        fun devolverProgresos(ct: Context):ArrayList<Progreso>{
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

        /**
         * devuelve la cantidad de instancias de un proceso para un cierto idEvento
         */
        fun checkProgresoExiste(idProgreso:Int, ct: Context):Short{
            var count: Short = 0
            doAsync {
                val db = UneAppDB(ct).readableDatabase

                try {
                    val sql = "select COUNT(idEvento) o from progreso_asistencia WHERE idEvento = '$idProgreso'"
                    val cursor = db.rawQuery(sql, null)
                    if (cursor.moveToFirst())
                        count = cursor.getShort(cursor.getColumnIndex("o"))
                    Log.d("numerosqlcheckprogreso", sql)
                    Log.d("numeroProgresoigual", count.toString())
                    cursor.close()
                } catch (e: Exception) {
                }
            }
            return count
        }

        /**
         * cambio el nombre evento por el idEvento
         */
        fun idEventoPorNombre(ct: Context, nombre: String):Int{
            var idEvento: Int = 0
            doAsync {
                val db = UneAppDB(ct).readableDatabase

                try {
                    val cursor = db.rawQuery("select _id from eventos where nombreEvento = $nombre", null)
                    if (cursor.moveToFirst())
                        idEvento = cursor.getInt(cursor.getColumnIndex("_id"))
                    cursor.close()
                }
                catch (e: Exception){Log.d("idEventoNoEncontrado", e.message)}
            }
            return idEvento
        }
    }
}