package com.example.clara.aprender;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Lista_Juego.BoardFragment;
import com.example.clara.aprender.Modelos.Nivel;

public class Juego extends AppCompatActivity {

    ImageButton IBSonido, IBAyuda, IBAtras, IBPlay, IBAdelante;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        int id_nivel = Integer.parseInt(getIntent().getStringExtra("id"));
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        Nivel nivel_actual = BDAprender.getNivelDAO().getNivelPorID(id_nivel);
        //Hay que añadir limitadores de elementos etc.

        if (savedInstanceState == null) {
            showFragment(BoardFragment.newInstance());
        }
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    //Como se hacen los botones?
    /*
    IBSonido.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, CreditosActivity.class);
            startActivity(i);
        }
    });
    */
    //Cada uno de estos corresponde a los botones.
    //Poner un cambio de sonido a muteado en la imagen, y poner cierto sonido cuando pulsamos play, aunque sea como test
    private void Sonido(){

    }
    // Poner un Android Tour? para explicar los diferentes items en la lista
    private void Ayuda(){

    }
    //Mover hacia atrás en la lista de elementos
    private void Atras(){

    }
    //Ir cada 0.5 segundos una instruccion hacia adelante
    //Tiene que coger los elementos de la columna 2, y lo tiene que comparar con una solucion?
    private void Jugar(){

    }
    //Cuando esta ejecutandose, el boton de play se cambia a parar, donde podemos parar el juego, el index de instrucciones se reinicia y se cambia el boton a play
    private void Parar(){

    }
    //Mover hacia adelante en la lista de elementos
    private void Adelante(){

    }

}
