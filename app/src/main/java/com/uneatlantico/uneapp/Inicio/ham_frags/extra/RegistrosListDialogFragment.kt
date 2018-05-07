package com.uneatlantico.uneapp.Inicio.ham_frags.extra

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.uneatlantico.uneapp.R;
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.recogerRegistrosEventoDia
import com.uneatlantico.uneapp.db.estructuras_db.Registro
import kotlinx.android.synthetic.main.fragment_registros_list_dialog.*


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    RegistrosListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [RegistrosListDialogFragment.Listener].
 */
class RegistrosListDialogFragment : BottomSheetDialogFragment() {
    private lateinit var listRegistros:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_registros_list_dialog, container, false)

        val idEvento = arguments!!.getInt("idEvento")
        val fecha = arguments!!.getString("fecha")
        listRegistros = v.findViewById(R.id.list_registros)
        val db =  registrosInsideDay(recogerRegistrosEventoDia(idEvento, this.context!!, fecha))
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context!!)
        listRegistros.layoutManager = layoutManager
        val registrosAdapter = RegistrosAdapter(db)
        listRegistros.adapter = registrosAdapter
        return v
    }

    private fun registrosInsideDay(registros:ArrayList<Registro>):ArrayList<Registro>{

        registros.forEach {
            it.fecha = formatfecha(it.fecha)
        }

        return registros
    }

    private fun formatfecha(fechaNoFormat:String):String {
        val trozosFecha = fechaNoFormat.split(' ')
        return trozosFecha[1]
    }

    private inner class RegistrosAdapter: RecyclerView.Adapter<RegistrosAdapter.RegistrosViewHolder> {

        val registros:ArrayList<Registro>

        constructor(registros:ArrayList<Registro>){
            this.registros = registros
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrosViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_registros_list_dialog_item, parent, false)
            return RegistrosViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RegistrosViewHolder, position: Int) {
            holder.fecha.text = registros[position].fecha
            if(registros[position].estado == 1)
                holder.background.setBackgroundColor(Color.argb(200,15,180,88))
            else
                holder.background.setBackgroundColor(Color.argb(200,255,0,0))
        }

        override fun getItemCount(): Int {
            return registros.size
        }

        //inner class RegViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        inner class RegistrosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var fecha: AppCompatTextView
            internal var background: RelativeLayout

            init {
                fecha = itemView.findViewById(R.id.fecha_completa_text)
                background = itemView.findViewById(R.id.backgroundList)
            }
        }
    }

    companion object {

        // TODO: Customize parameters
        /*fun newInstance(itemCount: Int): RegistrosListDialogFragment =
                RegistrosListDialogFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ITEM_COUNT, itemCount)
                    }
                }*/

    }
}
