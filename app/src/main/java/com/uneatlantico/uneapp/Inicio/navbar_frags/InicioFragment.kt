package com.uneatlantico.uneapp.Inicio.navbar_frags


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio.InicioAdapter
import com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio.InicioData
import com.uneatlantico.uneapp.Inicio.navbar_frags.recyview_frag_inicio.InicioSwipeDeleteCallback
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.WebViewActivity
import kotlinx.android.synthetic.main.card_layout.view.*


//https://github.com/kikoso/Swipeable-Cards
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InicioFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InicioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InicioFragment : Fragment() {

    fun newInstance(): Fragment {
        return InicioFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_inicio, container, false)
        val inicioData = InicioData(this.context!!)
        val inicioNoticias = inicioData.listaNoticias
        var recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)

        //creo el adaptador para el recyclerview y le pongo los eventos de click de la tarjeta y del boton de 3 puntos
        val inicioAdapter = InicioAdapter(inicioNoticias, object : InicioAdapter.InicioAdapterListener {
            override fun cardOnClick(v: View, position: Int) {
                Log.d("contenidoCartaNoticia", v.title.text.toString() + " " + v.fullText.text.toString())
                abrirLink(inicioNoticias[position].url, inicioNoticias[position].titulo)
            }
            override fun settingsOnClick(v: View, position: Int) {
                Log.d("botoncardajustes", "soy un boton")
                showPopupMenu(v.settings_3_dots, position)
            }

        }, this.context!!)

        recyclerView.adapter = inicioAdapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val swipeHandler = object : InicioSwipeDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as InicioAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    private fun abrirLink(url:String, titulo:String){
        val i = Intent(this.context, WebViewActivity::class.java)
        i.putExtra("url", url)
        i.putExtra("titulo", titulo)
        //finish()  //Kill the activity from which you will go to next activity
        startActivityForResult(i, 0)
    }

    private fun showPopupMenu(view: View, position: Int) {
        // inflate menu
        val popup = PopupMenu(view.context, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.drawer_view, popup.getMenu())
        popup.setOnMenuItemClickListener(MyMenuItemClickListener(position))
        popup.show()
    }

    companion object {
        fun newInstance(): InicioFragment = InicioFragment()
    }

    internal inner class MyMenuItemClickListener(private val position: Int) : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.getItemId()) {

                R.id.ham_notas -> {
                    Log.d("pulsasteiconoajustes", "holahola")
                    //Toast.makeText(this.context, "Add to favourite", Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.navigation_horario -> {

                    return true
                }
                R.id.navigation_campus -> return true
            }
            return false
        }
    }
}// Required empty public constructor
