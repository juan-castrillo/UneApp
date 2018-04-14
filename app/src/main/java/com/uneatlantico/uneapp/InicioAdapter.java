package com.uneatlantico.uneapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Juan CG on 25/03/2018.
 */

public class InicioAdapter extends RecyclerView.Adapter{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return InicioData.titulo.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemText;
        private ImageView mItemImage;
        private TextView mItemText2;

        public ListViewHolder(View itemView){
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.title);
            mItemImage = (ImageView) itemView.findViewById(R.id.thumbnail);
            mItemText2 = (TextView) itemView.findViewById(R.id.fullText);
            itemView.setOnClickListener(this);
        }

        public void bindView(int posicion){
            mItemText.setText(InicioData.titulo[posicion]);
            mItemImage.setImageResource(InicioData.picturePath[posicion]);
            mItemText2.setText(InicioData.resumenArticulo[posicion]);
        }
        public void onClick(View view){

        }

        //"@+id/thumbnail"
                //"@+id/title"
    }
}
