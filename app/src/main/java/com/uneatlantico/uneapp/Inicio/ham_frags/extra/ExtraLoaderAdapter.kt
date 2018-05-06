package com.uneatlantico.uneapp.Inicio.ham_frags.extra

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

class ExtraLoaderAdapter : RecyclerView.Adapter<ExtraLoaderAdapter.RegViewHolder> {
    private val mlec: List<Registro>
    val onClickListener: ExtraLoaderAdapterListener
    private val evento:String

    constructor(mlec: List<Registro>, evento:String, listener: ExtraLoaderAdapterListener) {
        this.mlec = mlec
        this.onClickListener = listener
        this.evento = evento
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.small_card_layout, parent, false)
        return RegViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RegViewHolder, position: Int) {
        if(position < 20) {
            holder.textViewFecha.text = mlec[position].fecha
            holder.textViewEvento.text = evento
            if (mlec[position].enviado == 0)
                holder.card.setCardBackgroundColor(Color.argb(150, 32, 151, 241))//holder.card.setCardBackgroundColor(getItemViewType(R.color.colorAccent))
            else
                holder.card.setCardBackgroundColor(Color.WHITE)
        }
        else{
            holder.textViewEvento.text = "Pulse para mas"
            holder.textViewEvento.textSize = 12F
            //holder.textViewEvento
            holder.textViewEvento.gravity = 1
            holder.textViewFecha.height = 0
            holder.card.elevation = 0F
            //holder.textViewEvento
            //TODO centrar el textview y aumentar tamaño
        }
        //holder.imageViewEnviado.setImageResource(R.drawable.tick_enviado)
    }

    override fun getItemCount(): Int {
        Log.d("nothing", mlec.size.toString())
        var size = mlec.size
        if(size == 20)
            size++
        return size
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

    interface ExtraLoaderAdapterListener {
        fun cardOnClick(v: View, position: Int)
        //fun viewMore(v:View, position: Int)
    }

}

