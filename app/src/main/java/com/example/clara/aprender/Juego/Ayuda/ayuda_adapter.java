package com.example.clara.aprender.Juego.Ayuda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clara.aprender.Adapters.RecyclerViewAdapter;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.R;

public class ayuda_adapter extends RecyclerView.Adapter<ayuda_adapter.ViewHolder> {

    private Context mContext ;
    private Nivel nivel ;
    private int[] imagenes;
    private String[] Descripciones;
    private String[] Titulos = {" ",
            "General",
            "Input",
            "Output",
            "Bump+",
            "Bump-",
            "CopyTo",
            "CopyFrom",
            "Sum",
            "Sub",
            "Jump"
    };


    public ayuda_adapter(Context mContext, Nivel nivel) {
        this.mContext = mContext;
        this.nivel = nivel;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Titulos = new String[]{nivel.getTitulo(),
                "General",
                "Input",
                "Output",
                "Bump+",
                "Bump-",
                "CopyTo",
                "CopyFrom",
                "Sum",
                "Sub",
                "Jump"
        };
        Descripciones = new String[]{nivel.getProblema(),
                "Ordena las instrucciones de la izquierda para crear una serie de instrucciones a la derecha.",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details",
                "Item one details"
        };
        imagenes = new int[]{ R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl,
                R.drawable.lvl
//            R.drawable.general,
//            R.drawable.input,
//            R.drawable.output,
//            R.drawable.bumpmas,
//            R.drawable.bumpmenos,
//            R.drawable.copyto,
//            R.drawable.copyfrom,
//            R.drawable.sum,
//            R.drawable.sub,
//            R.drawable.jump,
        };
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.nivel_item,parent,false);
        return new ayuda_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.Titulo.setText(Titulos[i]);
        viewHolder.Imagen.setImageResource(imagenes[i]);
        viewHolder.texto.setText(Descripciones[i]);
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Imagen;
        TextView Titulo, texto;
        CardView cardView ;

        public ViewHolder(View itemView) {
            super(itemView);

            Imagen =  itemView.findViewById(R.id.Imagen_Ayuda);
            Titulo =  itemView.findViewById(R.id.Titulo_Ayuda);
            texto =  itemView.findViewById(R.id.Texto_Ayuda);
            cardView = (CardView) itemView.findViewById(R.id.Ayuda_item);
        }
    }
}
