package com.uneatlantico.uneapp.Inicio.ham_frags

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.uneatlantico.uneapp.Inicio.InicioActivity
import com.uneatlantico.uneapp.Inicio.ham_frags.extra.ExtraAdapter
import com.uneatlantico.uneapp.Inicio.ham_frags.extra.ExtraLoaderActivity
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverProgresos
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.estadoUltimo
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.idEventoPorNombre
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.small_card_layout.view.*


/**
 * https://github.com/akexorcist/Android-RoundCornerProgressBar
 */
class ExtraActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra)

        val db = devolverProgresos(this)
        recyclerView = findViewById(R.id.bars_list)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        /*val extraAdapter = ExtraAdapter(db, ExtraAdapter.ExtraAdapterListener{
            override fun iconTextViewOnClick(v:View, position: Int){

            }
        })*/
        //TODO implementar click en barritas de progreso
        val extraAdapter = ExtraAdapter(db, object : ExtraAdapter.ExtraAdapterListener {
            override fun iconTextViewOnClick(v: View, position: Int) {
                //Log.d("extraclick", v.eventoTextView.text.toString())
                showEvento(db[position].Evento)
            }

            /*override fun iconImageViewOnClick(v: View, position: Int) {

            }

            override fun iconImageUnFollowOnClick(v: View, position: Int) {

            }*/
        })
        recyclerView.adapter = extraAdapter

        //recyclerView.addOnItemTouchListener(ExtraRecyclerViewClickListener(this, recyclerView, ExtraRecyclerViewClickListener.OnItemClickListener?)



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra("title")
    }

    fun showEvento(evento:String){
        /*val idEvento = idEventoPorNombre(this, evento)
        estadoUltimo(this, )*/
        val i = Intent(this, ExtraLoaderActivity::class.java)
        i.putExtra("evento", evento)
        startActivity(i)
        //Log.d("jsonaccount" ,acct.toJson());
        //Kill the activity from which you will go to next activity

        //finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
