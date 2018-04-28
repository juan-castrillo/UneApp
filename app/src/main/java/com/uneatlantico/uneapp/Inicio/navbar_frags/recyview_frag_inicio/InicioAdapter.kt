package com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.estructuras_db.Noticia
import java.util.*


class InicioAdapter : RecyclerView.Adapter<InicioAdapter.InicioViewHolder> {
    private val listaNoticias: ArrayList<Noticia>
    val onClickListener: InicioAdapterListener
    val ct:Context
    constructor(listaNoticias: ArrayList<Noticia>, listener: InicioAdapterListener, ct: Context) {
        this.listaNoticias = listaNoticias
        this.onClickListener = listener
        this.ct = ct
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InicioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return InicioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InicioViewHolder, position: Int) {
        holder.tituloNoticia.text = listaNoticias[position].titulo
        holder.contenidoNoticia.text = listaNoticias[position].contenido
        //holder.imagenNoticia.setImageBitmap(listaNoticias[position].imagen)

        Picasso.with(ct).load(listaNoticias[position].imagenUrl).into(holder.imagenNoticia)

    }

    /*fun addItem(name: String) {
        listaNoticias.add(name)
        notifyItemInserted(items.size)
    }*/

    fun removeAt(position: Int) {
        listaNoticias.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        //Log.d("nothing", listaNoticias.size.toString())
        return listaNoticias.size
    }

    inner class InicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        var tituloNoticia: AppCompatTextView
        var contenidoNoticia: AppCompatTextView
        var imagenNoticia: ImageView
        var settingsImage: ImageView
        var card:CardView
        init {
            //emptyView = (TextView) itemView.findViewById(R.id.emptyView);
            tituloNoticia = itemView.findViewById(R.id.title) as AppCompatTextView
            contenidoNoticia = itemView.findViewById(R.id.fullText) as AppCompatTextView
            imagenNoticia = itemView.findViewById(R.id.thumbnail) as ImageView
            settingsImage = itemView.findViewById(R.id.settings_3_dots) as ImageView
            card = itemView.findViewById(R.id.card_view) as CardView

            //listeners
            card.setOnClickListener { v -> onClickListener.cardOnClick(v, adapterPosition) }
            settingsImage.setOnClickListener { v -> onClickListener.settingsOnClick(v, adapterPosition) }
        }

    }

    interface InicioAdapterListener {
        fun cardOnClick(v: View, position: Int)
        fun settingsOnClick(v:View, position: Int)
    }

}