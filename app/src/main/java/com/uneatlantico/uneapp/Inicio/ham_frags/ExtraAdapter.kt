package com.uneatlantico.uneapp.Inicio.ham_frags

import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg.RegistroAsistenciaAdapter
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.Registro

class ExtraAdapter : RecyclerView.Adapter<ExtraAdapter.RegViewHolder> {
    private val mlec: List<Registro>

    constructor(mlec: List<Registro>) {
        this.mlec = mlec
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false)
        return RegViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RegViewHolder, position: Int) {
        holder.textViewEvento.text = mlec[position].Evento
        //holder.roundCornerProgressBar.progress = mlec[position]

        //holder.imageViewEnviado.setImageResource(R.drawable.tick_enviado)
    }

    override fun getItemCount(): Int {
        Log.d("nothing", mlec.size.toString())
        return mlec.size
    }

    class RegViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewEvento: AppCompatTextView
        var roundCornerProgressBar: RoundCornerProgressBar
        init {
            //emptyView = (TextView) itemView.findViewById(R.id.emptyView);
            textViewEvento = itemView.findViewById(R.id.progress_text_element) as AppCompatTextView
            roundCornerProgressBar = itemView.findViewById(R.id.progress_bar_element) as RoundCornerProgressBar
        }
    }
}