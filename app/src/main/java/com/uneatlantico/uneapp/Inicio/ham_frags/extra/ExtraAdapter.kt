package com.uneatlantico.uneapp.Inicio.ham_frags.extra

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.estructuras_db.Progreso

class ExtraAdapter : RecyclerView.Adapter<ExtraAdapter.ExtraViewHolder> {
    private val mlec: List<Progreso>
    val onClickListener: ExtraAdapterListener

    constructor(mlec: List<Progreso>, listener:ExtraAdapterListener) {
        this.mlec = mlec
        this.onClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false)
        return ExtraViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExtraViewHolder, position: Int) {
        holder.textViewEvento.text = mlec[position].Evento
        val progreso = (mlec[position].horasAlumno/mlec[position].horasEventoTotales)*100
        Log.d("progresoAsignatura", progreso.toString())
        holder.roundCornerProgressBar.progress = progreso
    }

    override fun getItemCount(): Int {
        Log.d("nothing", mlec.size.toString())
        return mlec.size
    }

    inner class ExtraViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var textViewEvento: AppCompatTextView
        internal var roundCornerProgressBar: RoundCornerProgressBar
        internal var linearLayout: LinearLayout

        init {
            textViewEvento = itemView.findViewById(R.id.progress_text_element)
            roundCornerProgressBar = itemView.findViewById(R.id.progress_bar_element)
            linearLayout = itemView.findViewById(R.id.linear_general_extra)
            linearLayout.setOnClickListener { v -> onClickListener.iconTextViewOnClick(v, adapterPosition) }
        }
    }

    interface ExtraAdapterListener {
        fun iconTextViewOnClick(v: View, position: Int)
        /*fun iconImageViewOnClick(v: View, position: Int)

        fun iconImageUnFollowOnClick(v: View, position: Int)*/
    }
}