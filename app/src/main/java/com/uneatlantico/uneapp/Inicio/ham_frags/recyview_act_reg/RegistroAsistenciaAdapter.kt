package com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg

import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.estructuras_db.Registro


/**
 * https://stackoverflow.com/questions/44714815/populate-sqlite-data-to-recyclerview-list
 */
class RegistroAsistenciaAdapter : RecyclerView.Adapter<RegistroAsistenciaAdapter.RegViewHolder> {
    private val mlec: List<Registro>
    val onClickListener: RegistroAsistenciaAdapterListener

    constructor(mlec: List<Registro>, listener: RegistroAsistenciaAdapterListener) {
        this.mlec = mlec
        this.onClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.small_card_layout, parent, false)
        return RegViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RegViewHolder, position: Int) {
        holder.textViewFecha.text = mlec[position].fecha
        holder.textViewEvento.text = mlec[position].Evento
        if(mlec[position].enviado == 0)
                //holder.card.setCardBackgroundColor(getItemViewType(R.color.colorAccent))
            holder.card.setCardBackgroundColor(Color.BLUE)
        //holder.imageViewEnviado.setImageResource(R.drawable.tick_enviado)
    }

    override fun getItemCount(): Int {
        Log.d("nothing", mlec.size.toString())
        return mlec.size
    }

    inner class RegViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewEvento: AppCompatTextView
        var textViewFecha: AppCompatTextView
        var imageViewEnviado: ImageView
        var card:CardView
        init {
            //emptyView = (TextView) itemView.findViewById(R.id.emptyView);
            textViewEvento = itemView.findViewById(R.id.eventoTextView) as AppCompatTextView
            textViewFecha = itemView.findViewById(R.id.fechaTextView) as AppCompatTextView
            imageViewEnviado = itemView.findViewById(R.id.stateImage) as ImageView
            card = itemView.findViewById(R.id.card_view_registro) as CardView
            card.setOnClickListener { v -> onClickListener.cardOnClick(v, adapterPosition) }
        }
    }

    interface RegistroAsistenciaAdapterListener {
        fun cardOnClick(v: View, position: Int)
    }

}