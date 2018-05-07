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
        db.close()
        return id
    }
    companion object {

        /**
         * devuelve todos los registros de todas las materias
         * para mostrar en registroAsistenciaActivity
         */
        fun recogerRegistros(ct: Context): ArrayList<Registro> {
            val registros = ArrayList<Registro>()
            //doAsync {
                //Log.d("rutaDB", DB_PATH)
                val db = UneAppDB(ct).readableDatabase
                lateinit var cursor: Cursor
                try {
                    cursor = db.rawQuery("select e.nombreEvento idEvento,r.fecha fecha,r.enviado enviado,r.estado estado from registros r, eventos e where r.idEvento = e._id ORDER BY _id desc", null)
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
            db.close()

            //}
            return registros
        }

        fun recogerFechasEvento(ct: Context, idEvento: Int):ArrayList<Registro>{
            val registros = ArrayList<Registro>()
            //doAsync {
            //Log.d("rutaDB", DB_PATH)
            val db = UneAppDB(ct).readableDatabase
            lateinit var cursor: Cursor
            try {
                val sql = "select fecha, enviado from registros where idEvento = '$idEvento' and estado = 0 ORDER BY _id desc LIMIT 20"
                cursor = db.rawQuery(sql, null)
                //Log.d("sqlExtraRegistro", sql)
            } catch (e: Exception) {
                Log.d("queryRecogerAll", e.message)
            }
            try {
                //var id: Int
                //var Evento: String
                var fecha: String
                //var estado: Int
                var enviado: Int
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast) {
                        //id = cursor.getInt(cursor.getColumnIndex("_id"))
                        //Evento = cursor.getString(cursor.getColumnIndex("nombreEvento"))
                        fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                        //estado = cursor.getInt(cursor.getColumnIndex("estado"))
                        enviado = cursor.getInt(cursor.getColumnIndex("enviado"))

                        registros.add(Registro(fecha = fecha, enviado = enviado))
                        cursor.moveToNext()
                    }
                }
            } catch (x: Exception) {
                Log.d("asignacionRecogerAll", x.message)
            }
            cursor.close()
            db.close()


            return registros
        }

        fun recogerRegistrosEventoDia(idEvento:Int, ct: Context, fecha: String):ArrayList<Registro>{
            val registros = ArrayList<Registro>()
            //doAsync {
            //Log.d("rutaDB", DB_PATH)
            val db = UneAppDB(ct).readableDatabase
            try {
                val sql = "select fecha, enviado, estado from registros where idEvento = '$idEvento' and fecha LIKE '%$fecha%' ORDER BY _id desc"
                val cursor = db.rawQuery(sql, null)
                //Log.d("sqlExtraRegistro", sql)

                //var id: Int
                //var Evento: String
                var fecha: String
                var estado: Int
                var enviado: Int
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast) {
                        //id = cursor.getInt(cursor.getColumnIndex("_id"))
                        //Evento = cursor.getString(cursor.getColumnIndex("nombreEvento"))
                        fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                        estado = cursor.getInt(cursor.getColumnIndex("estado"))
                        enviado = cursor.getInt(cursor.getColumnIndex("enviado"))

                        registros.add(Registro(fecha = fecha, enviado = enviado, estado = estado))
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            } catch (x: Exception) {
                Log.d("asignacionRecogerAll", x.message)
            }

            db.close()


            return registros
        }

        //TODO tomar solo los 20 primeros registros
        fun recogerRegistros(ct: Context, idEvento: Int): ArrayList<Registro> {
            val registros = ArrayList<Registro>()
            //doAsync {
            //Log.d("rutaDB", DB_PATH)
            val db = UneAppDB(ct).readableDatabase
            lateinit var cursor: Cursor
            try {
                val sql = "select fecha, enviado, nombreEvento from registros inner join Eventos on registros.idEvento = eventos._id where idEvento = '$idEvento'  ORDER BY fecha desc LIMIT 20"
                cursor = db.rawQuery(sql, null)
                //Log.d("sqlExtraRegistro", sql)
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
                        Evento = cursor.getString(cursor.getColumnIndex("nombreEvento"))
                        fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                        //estado = cursor.getInt(cursor.getColumnIndex("estado"))
                        enviado = cursor.getInt(cursor.getColumnIndex("enviado"))
                        Log.d("miEvento", Evento)
                        registros.add(Registro(Evento = Evento, fecha = fecha, enviado = enviado))
                        cursor.moveToNext()
                    }
            } catch (x: Exception) {
                Log.d("asignacionRecogerAll", x.message)
            }
            cursor.close()
            db.close()

            //}
            return registros
        }

        /**
         * Devuelve el ultimo estado de una materia concreta para conocer
         * que estado se debe insertar en el siguiente registro
         */
        fun estadoUltimo(ct: Context, idEvento: Int, fechahoy:String = ""):Int{
            var estado: Int = 0
            //doAsync {
                //lateinit var cursor: Cursor
                val db = UneAppDB(ct).readableDatabase
                try {
                    val cursor = db.rawQuery("select estado from registros WHERE idEvento = $idEvento AND fecha LIKE '%$fechahoy%' ORDER BY _id DESC LIMIT 1", null)

                    if (cursor.moveToFirst())
                        while (!cursor.isAfterLast) {
                            estado = cursor.getString(cursor.getColumnIndex("estado")).toInt()
                            cursor.moveToNext()
                        }
                    Log.d("estadoultimoDB", estado.toString())
                    cursor.close()
                } catch (e: Exception) {
                    Log.d("error consiguiendo ultimo registro", e.message)
                }
            db.close()
            //}
            return estado
        }


        fun ultimoRegistro(ct: Context):Registro{
            var registro:Registro = Registro()
            val db = UneAppDB(ct).readableDatabase
            try {
                val cursor = db.rawQuery("select * from registros ORDER BY _id DESC LIMIT 1", null)

                if (cursor.moveToFirst()) {
                    val estado = cursor.getInt(cursor.getColumnIndex("estado"))
                    val idEvento = cursor.getInt(cursor.getColumnIndex("idEvento"))
                    val fecha = cursor.getString(cursor.getColumnIndex("fecha"))
                    Log.d("estado", estado.toString())
                    registro = Registro(estado = estado, idEvento = idEvento, fecha = fecha)
                }

                cursor.close()
            } catch (e: Exception) {
                Log.d("error consiguiendo ultimo registro", e.message)
            }
            db.close()
            return registro
        }

        /**
         * devuelve un unico usuario de google con todos sus datos
         */
        fun devolverUsuario(ct: Context):List<String> {
            var GsignIn:List<String> = emptyList()
            //doAsync {
                val db = UneAppDB(ct).readableDatabase
                //var idPersona:String
                var nombre: String
                var email: String
                var photoUrl: String

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
                } catch (e: Exception) {
                    Log.d("usuarioNoEncontradoEnDB", e.message)
                }
            db.close()
            //}
            return GsignIn
        }

        /**
         * devuelve el id de la persona
         */
        fun getIdPersona(ct: Context):String{
            var idPersona = ""
            //doAsync {
                val db = UneAppDB(ct).readableDatabase
                try {
                    val cursor = db.rawQuery("select idPersona from usuario", null)
                    if (cursor.moveToFirst())
                        idPersona = cursor.getString(cursor.getColumnIndex("idPersona"))
                    cursor.close()
                } catch (e: Exception) {

                }
            db.close()
            //}
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
        fun getRegistro(idEvento: Int, fecha:String, ct: Context):Registro{
            var registro:Registro = Registro()

            //doAsync {
                val db = UneAppDB(ct).readableDatabase
                var _id:Int
                var enviado:Int
                var estado:Int
                try {
                    val sql = "select * from registros where idEvento = '$idEvento' AND fecha = '$fecha'"
                    val cursor = db.rawQuery(sql, null)
                    Log.d("getRegistrosql", sql)
                    if (cursor.moveToFirst()) {

                            _id = cursor.getInt(cursor.getColumnIndex("_id"))
                            estado = cursor.getInt(cursor.getColumnIndex("estado"))
                            enviado = cursor.getInt(cursor.getColumnIndex("enviado"))
                            //registro.fecha = fecha

                            registro = Registro(_id, idEvento, fecha = fecha, estado = estado, enviado = enviado)

                    }
                    cursor.close()

                }
                catch (e: Exception){Log.d("registroNoEncontrado", e.message)}
            db.close()
            //}
            //Log.d("eventoSelect", registro.estado.toString())
            return registro
        }

        /**
         * Cambio el campo enviado de un registro concreto al ser mandado al WS
         */
        fun updateRegistro(updateList:List<String>, ct: Context) {
            doAsync {
                val db = UneAppDB(ct).writableDatabase
                val sql = "UPDATE registros set enviado = 1 WHERE idEvento = ${updateList[1]} AND fecha = '${updateList[2]}'"
                db.execSQL(sql)
                db.close()
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
            //doAsync {

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
                    db.close()
                }
                catch (e: Exception) {
                    Log.d("progresosNoTraidos", e.message)}
            //}
            return progresos
        }

        /**
         * devuelve la cantidad de instancias de un proceso para un cierto idEvento
         */
        fun checkProgresoExiste(idProgreso:Int, ct: Context):Short{
            var count: Short = 0
            //doAsync {
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
            db.close()
            //}
            return count
        }

        /**
         * cambio el nombre evento por el idEvento
         */
        fun idEventoPorNombre(ct: Context, nombre: String):Int{
            var idEvento: Int = 0
            //doAsync {
                val db = UneAppDB(ct).readableDatabase

                try {
                    val cursor = db.rawQuery("select _id from eventos where nombreEvento = '$nombre'", null)
                    if (cursor.moveToFirst())
                        idEvento = cursor.getInt(cursor.getColumnIndex("_id"))
                    cursor.close()
                }
                catch (e: Exception){Log.d("idEventoNoEncontrado", e.message)}
            //}
            db.close()
            return idEvento
        }
    }
}