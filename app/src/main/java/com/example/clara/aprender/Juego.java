package com.example.clara.aprender;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Lista_Juego.BoardFragment;
import com.example.clara.aprender.Modelos.Nivel;

public class Juego extends AppCompatActivity {

    ImageButton IBSonido, IBAyuda, IBAtras, IBPlay, IBAdelante;
    String Input_ini;
    String Output_ini;
    TextView Input, Output;
    boolean EstadoMusica;
    MediaPlayer musica;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        EstadoMusica=true;
        init();

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
        Toast toast;

        if(EstadoMusica) {
            //Para silenciar la canción
            EstadoMusica = false;
            musica.pause();




            toast=Toast.makeText(getApplicationContext(),"La cancion se ha parado",Toast.LENGTH_SHORT);
        }else{
            int length = musica.getCurrentPosition();
            musica.seekTo(length);
            musica.start();
            EstadoMusica= true;
            toast=Toast.makeText(getApplicationContext(),"La cancion ha vuelto",Toast.LENGTH_SHORT);

        }
        toast.show();
        // Mirar como hacer esto -> IBSonido.setImageResource(R.drawable.ic_lock_silent_mode_off);
    }
    // Poner un Android Tour? para explicar los diferentes items en la lista
    private void Ayuda(){

    }
    //Ir cada 0.5 segundos una instruccion hacia adelante
    //Tiene que coger los elementos de la columna 2, y lo tiene que comparar con una solucion?
    private void Jugar(){

    }
    //Cuando esta ejecutandose, el boton de play se cambia a parar, donde podemos parar el juego, el index de instrucciones se reinicia y se cambia el boton a play
    private void Parar(){

    }
    //Mover hacia atrás en la lista de elementos
    private void Atras(){

    }
    //Mover hacia adelante en la lista de elementos
    private void Adelante(){

    }

    private void init(){
        Input = findViewById(R.id.Input);
        Output = findViewById(R.id.Output);
        musica = MediaPlayer.create(getApplicationContext(), R.raw.musica);
        musica.setLooping(true);
        musica.start();
        IBSonido = findViewById(R.id.IBSonido);
        IBSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sonido();
            }
        });

        // Con el id que le pasamos desde la parte de niveles, hacemos una busqueda que cargaremos en la pantalla.
        int id_nivel = getIntent().getExtras().getInt("id");
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        Nivel nivel_actual = BDAprender.getNivelDAO().getNivelPorID(id_nivel);
        // De momento se pone en un textView,
        Input_ini = nivel_actual.getInput();
        Input.setText(Input_ini);
        Output_ini = nivel_actual.getOutput();
        Output.setText(Output_ini);
    }

}
