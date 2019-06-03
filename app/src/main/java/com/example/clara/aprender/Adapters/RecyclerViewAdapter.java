package com.example.clara.aprender.Adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clara.aprender.Juego.Juego;
import com.example.clara.aprender.LoadGame;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.R;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Nivel> mData ;


    public RecyclerViewAdapter(Context mContext, List<Nivel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.nivel_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        // Cambiar de vuelta a esto -> holder.lvl_img.setImageResource(mData.get(holder.getAdapterPosition()).getImagen()); <- o no utilizar una base de datos y utilizar archivos del movil de la carpeta drawable.

        holder.lvl_img.setImageResource(R.drawable.star);

        //prueba de que hace click, borrar luego
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "CLICK EN "+mData.get(position).getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });


        //SE INICIALIZAN LOS ITEMS DE CADA NIVEL Y SE ENVIA LA INFORMACION AL INTENT DE NIVEL
        //CON UN LISTENER EN CADA CARDVIEW DEL MENU DE NIVELES
        //holder.titulo_nivel.setText(mData.get(position).getTitulo());
        //holder.espacios.setImageResource(mData.get(position).getEspacios());
        //holder.resultado.setImageResource(mData.get(position).getResultado());
        //holder.solucion.setImageResource(mData.get(position).getSolucion());
        //holder.id.setImageResource(mData.get(position).getIdNivel());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoadGame.class);
                intent.putExtra("id",mData.get(position).getIdNivel());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView lvl_img;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            lvl_img = (ImageView) itemView.findViewById(R.id.level_img);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}