package com.example.clara.aprender.Juego.Ayuda;

import android.content.Context;
import android.util.Log;
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
            "Inputs y Outputs",
            "Lista de elementos",
            "Lista de instrucciones",
            "Input",
            "Output",
            "Bump+",
            "Bump-",
            "Holders",
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
                "Inputs y Outputs",
                "Lista de elementos",
                "Lista de instrucciones",
                "Input",
                "Output",
                "Bump+",
                "Bump-",
                "Holders",
                "CopyTo",
                "CopyFrom",
                "Sum",
                "Sub",
                "Jump"
        };
        Descripciones = new String[]{nivel.getProblema(),
                "Ordena las instrucciones de la izquierda para crear una serie de instrucciones a la derecha. La finalidad es que todos los elementos de la parte Input acaben en la parte Output.",
                "En la parte izquierda puedes  observar 2 cadenas de Objetos, una representa la entrada de objetos y la otra la salida.",
                "Aparecen bloques que significan una instrucción en el juego.",
                "Es una lista creada a partir de los elementos que son arrastrados desde la lista de los elementos.",
                "Coge el elemento del input, y es el foco de las instrucciones",
                "Entrega el elemento actual",
                "Añade 1 al elemento actual",
                "Resta 1 al elemento actual",
                "Sirven para guardar un elemento del input allí, para poder manipularlo en el futuro",
                "Copia el objeto al holder.",
                "Copia el objeto del holder",
                "Suma el valor del holder al objeto",
                "Resta el valor del holder al objeto",
                "Realiza un salto al lugar indicado",
        };
        imagenes = new int[]{ R.drawable.lvl,
                R.drawable.general,
                R.drawable.inputs_outputs,
                R.drawable.lista_elementos,
                R.drawable.lista_instrucciones,
                R.drawable.input,
                R.drawable.output,
                R.drawable.bumpmas,
                R.drawable.bumpmenos,
                R.drawable.holders,
                R.drawable.copyto,
                R.drawable.copyfrom,
                R.drawable.sum,
                R.drawable.sub,
                R.drawable.jump,
        };
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.ayuda_item,parent,false);
        return new ayuda_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int posicion) {
        Log.i("Lista Titulos",posicion+"");
        Log.i("Lista Titulos",Titulos[0]);
        holder.Titulo.setText(Titulos[posicion]);
        holder.Imagen.setImageResource(imagenes[posicion]);
        holder.texto.setText(Descripciones[posicion]);
    }

    @Override
    public int getItemCount() {
        return Titulos.length;
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
