package com.uneatlantico.uneapp.Inicio.ham_frags.recyview_act_reg

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uneatlantico.uneapp.R

class RegistroAsistenciaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
lateinit var r:RegistroAsistenciaData
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.small_card_layout, parent, false)
        r = RegistroAsistenciaData()
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        //return RegistroAsistenciaActivity().idEvento.size
        //return r.idEvento.size
        return r.idEvento.size
    }

    //TODO descomentar mItemImage para el estado (cuando se cree el campo aprovado en la base de datos)
    private inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val mItemText: TextView
        //private val mItemImage: ImageView
        private val mItemText2: TextView

        init {
            mItemText = itemView.findViewById<View>(R.id.eventoTextView) as TextView
            //mItemImage = itemView.findViewById<View>(R.id.stateImage) as ImageView
            mItemText2 = itemView.findViewById<View>(R.id.fechaTextView) as TextView
            itemView.setOnClickListener(this)
        }

        fun bindView(posicion: Int) {
           mItemText.text = r.idEvento[posicion].toString()
            //mItemImage.setImageResource(RegistroAsistenciaData.[posicion])
            mItemText2.text = r.fecha[posicion]
        }

        override fun onClick(view: View) {

        }

        //"@+id/thumbnail"
        //"@+id/title"
    }
}