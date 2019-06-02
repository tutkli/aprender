package com.example.clara.aprender.Juego;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.R;
import com.woxthebox.draglistview.BoardView;

public class Juego extends AppCompatActivity {

    ImageButton IBSonido, IBAyuda, IBAtras, IBPlay, IBAdelante;
    String Input_ini;
    String Output_ini;
    TextView Input, Output;
    boolean EstadoMusica;
    MediaPlayer musica;
    static Nivel nivel_actual;
    boolean juego_start;
    Toast Pruebas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        EstadoMusica=true;
        juego_start=false;
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
        if(EstadoMusica) {
            //Para silenciar la canción
            EstadoMusica = false;
            musica.pause();
            IBSonido.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode));
        }else{
            int length = musica.getCurrentPosition();
            musica.seekTo(length);
            musica.start();
            EstadoMusica= true;
            IBSonido.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off));
        }
        // Mirar como hacer esto -> IBSonido.setImageResource(R.drawable.ic_lock_silent_mode_off);
    }
    // Poner un Android Tour? para explicar los diferentes items en la lista
    private void Ayuda(){
        //Mostrar un cardview con
    }
    //Ir cada 0.5 segundos una instruccion hacia adelante
    //Tiene que coger los elementos de la columna 2, y lo tiene que comparar con una solucion?
    private void Jugar(){
        // Recorrer la lista
        // Poner un switch, que según el elemento de la lista, haga distintas cosas.
        IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));


    }
    //Cuando esta ejecutandose, el boton de play se cambia a parar, donde podemos parar el juego, el index de instrucciones se reinicia y se cambia el boton a play
    private void Parar(){
        // Poner el estado de restart
        // Cambiar todos los elementos a color normal
        // Reiniciar el contador
        // Reiniciar las animaciones
        IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));

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
        IBAyuda = findViewById(R.id.IBAyuda);
        IBAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ayuda();
            }
        });
        IBPlay = findViewById(R.id.IBPlay);
        IBPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(juego_start){
                    juego_start=false;
                    Parar();
                }else{
                    juego_start=true;
                    Jugar();
                }
            }
        });
        IBAdelante = findViewById(R.id.IBAdelante);
        IBAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adelante();
            }
        });
        IBAtras = findViewById(R.id.IBAtras);
        IBAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Atras();
            }
        });


        // Con el id que le pasamos desde la parte de niveles, hacemos una busqueda que cargaremos en la pantalla.
        int id_nivel = getIntent().getExtras().getInt("id");
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        nivel_actual = BDAprender.getNivelDAO().getNivelPorID(id_nivel);
        // De momento se pone en un textView,
        Input_ini = nivel_actual.getInput();
        Input.setText(Input_ini);
        Output_ini = nivel_actual.getOutput();
        Output.setText(Output_ini);
    }
    @Override
    public void onBackPressed() {
        musica.pause();
        finish();
    }

    public void DetectorElementos(String Instruccion){
        if(Instruccion.equals("input") || Instruccion.equals("output") ||Instruccion.equals("bumpmas") ||Instruccion.equals("bumpmenos"))
        switch (Instruccion){
            case "input": Input();
            case "output": Output();
            case "bumpmas": BumpMas();
            case "bumpmenos": BumpMenos();
        }else{
            if(Instruccion.contains(" ")){
                String[] InstruccionesArray = Instruccion.split(" ");
                switch (InstruccionesArray[0]) {
                    case "copyto":
                        copyTo(InstruccionesArray[1]);
                    case "copyfrom":
                        copyFrom(InstruccionesArray[1]);
                    case "sum":
                        Sum(InstruccionesArray[1]);
                    case "sub":
                        Sub(InstruccionesArray[1]);
                    case "jump":
                        Jump(InstruccionesArray[1]);
                }
            }
            }
    }

    // Métodos del juego.
    // El objeto es un cuadro.
    // Inicio: es el estado por defecto, a lo que vuelve la caja.

    public void Input(){

    }
    // Mueve la caja de inicio, que necesita un valor dentro y lo mueve a la parte de output, y elimina el valor de la caja, y vuelve al inicio
    public void Output(){

    }
    // Suma 1 al valor del cuadro, y le hace zoom (al cuadro) por unos milisegundos, para mostrar que está ocurriendo algo.
    public void BumpMas(){

    }
    // Lo mismo pero le quita 1
    public void BumpMenos(){

    }
    // mueve el objeto a uno de los holders, y lo rellena con el cuadro y el valor (cambia el fondo del cuadrado.), y vuelve a la posición inicial
    public void copyTo(String holder){

    }
    // mueve el objeto al holder y cambia el valor del objeto al contenido del holder, y vuelve a la posición inicio
    public void copyFrom(String holder){

    }
    // Coge el objeto de la zona de inicio, lo lleva a uno de las posiciones de los holders, y le añade realiza la suma
    public void Sum(String holder){

    }
    // El objeto de la zona de inicio, va al holder marcado, hace la resta, el resultado se queda en el objeto y vuelve al inicio
    public void Sub(String holder){

    }
    public void Jump(String Lugar){
        // Diferentes Jump
        //Jump
        //Jump if zero
        //Jump if negative
        //Jump if not number
    }
    // Si mientras se ejecuta, ocurre algún error en la ejecución, con las reglas, te lo mostraría con este método, hace que ele elemento se ponga en rojo en la lista.
    public void ErrorEnEjecución(){

    }
    // Muestra la calificación, en un cardview y la guarda en la base de datos.
    public void Victoria(){

    }

}
