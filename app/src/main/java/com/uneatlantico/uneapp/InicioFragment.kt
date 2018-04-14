package com.uneatlantico.uneapp


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


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
        var recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)
        val inicioAdapter = InicioAdapter()
        recyclerView.adapter = inicioAdapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        //val inicioAdapter = InicioAdapter()

        //recyclerView.adapter

        //var layoutManager = recyclerView.layoutManager
        return view
    }






    // Fetching items, passing in the View they will control.

    companion object {
        fun newInstance(): InicioFragment = InicioFragment()
    }
}// Required empty public constructor
