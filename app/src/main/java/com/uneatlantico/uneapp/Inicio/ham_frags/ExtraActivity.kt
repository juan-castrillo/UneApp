package com.uneatlantico.uneapp.Inicio.ham_frags

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.uneatlantico.uneapp.Inicio.ham_frags.extra.ExtraAdapter
import com.uneatlantico.uneapp.R
import com.uneatlantico.uneapp.db.UneAppExecuter.Companion.devolverProgresos
import kotlinx.android.synthetic.main.progress_bar.view.*


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
                Log.d("onClick listener funciono", v.progress_text_element.text.toString())

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


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
