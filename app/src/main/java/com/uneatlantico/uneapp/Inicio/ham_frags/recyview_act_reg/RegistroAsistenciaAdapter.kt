package com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.Registro


/**
 * https://stackoverflow.com/questions/44714815/populate-sqlite-data-to-recyclerview-list
 */
class RegistroAsistenciaAdapter : RecyclerView.Adapter<RegistroAsistenciaAdapter.RegViewHolder> {
    private val mlec: List<Registro>

    constructor(mlec: List<Registro>) {
        this.mlec = mlec
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.small_card_layout, parent, false)
        return RegViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RegViewHolder, position: Int) {
        holder.textViewLecTime.text = mlec[position].idEvento.toString()
        holder.textViewSub.text = mlec[position].fecha
    }

    override fun getItemCount(): Int {
        Log.d("nothing", mlec.size.toString())
        return mlec.size
    }

    class RegViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewSub: AppCompatTextView
        var textViewLecTime: AppCompatTextView

        init {
            //emptyView = (TextView) itemView.findViewById(R.id.emptyView);
            textViewSub = itemView.findViewById(R.id.eventoTextView) as AppCompatTextView
            textViewLecTime = itemView.findViewById(R.id.fechaTextView) as AppCompatTextView
        }
    }
    /*class RegViewHolder: RecyclerView.ViewHolder() {

        private val textViewSub: TextView
        private val textViewLecTime: TextView

        constructor(val itemView: View) {
            super(itemView)
            //emptyView = (TextView) itemView.findViewById(R.id.emptyView);
            textViewSub = itemView.findViewById(R.id.eventoTextView) as TextView
            textViewLecTime = itemView.findViewById(R.id.fechaTextView) as TextView
        }
    }*/
}