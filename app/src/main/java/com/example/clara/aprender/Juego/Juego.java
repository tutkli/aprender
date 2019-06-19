package com.example.clara.aprender.Juego;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Juego.Ayuda.ayuda;
import com.example.clara.aprender.LoadGame;
import com.example.clara.aprender.MenuNivelActivity;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.Modelos.Solucion;
import com.example.clara.aprender.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Juego extends AppCompatActivity{


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    DatabaseReference myRef = database.getReference("usuarios");
    FirebaseUser firebaseUser;
    GoogleSignInAccount googleUser;
    //Valores para el juego

    public int color_caja_letra;
    public int color_caja_numero;
    public int color_fondo_juego;
    public int color_color_error;

    static Nivel nivel_actual;
    // Los inputs que tenemos
    ImageButton IBAtras, IBPlay, IBAdelante, IBAyuda, IBSonido;
    TextView Input_1,Input_2,Input_3,Actual,Output_1,Output_2,Output_3, Holder_1, Holder_2, Holder_3, Holder_4;
    // Coordenadas.
    float IX=100, IO1Y=300, IO2Y=450, IO3Y=600, AX=400, AY=100 ,OX=700, HIX=350, HDX=500, HAY=350, HDY=500;
    Animaciones A;
    // Listas que definen las instrucciones, los elementos de entrada y los elementos de salida
    List<String> Entrada, Salida, Instrucciones, Resultado;
    // Valor Actual y enunciado
    String Actual_Valor, Problema, InstruccionesString;
    // Para saber en que instruccion se encuentra, Milisegundos para que se ejectue esta parte y numero de intentos
    int CElemento, CSElemento, x, num_intentos, JUMPA,JUMPB,JUMPC,JUMPD, CInstruccion, iJump;
    MediaPlayer musica;
    boolean Jugando=false, EstadoMusica;
    // Librería utilizada https://github.com/florent37/ViewAnimator



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        //Todos los views
        Input_1 = findViewById(R.id.Input_1);
        Input_2 = findViewById(R.id.Input_2);
        Input_3 = findViewById(R.id.Input_3);
        Actual = findViewById(R.id.Actual);
        Output_1 = findViewById(R.id.Output_1);
        Output_2 = findViewById(R.id.Output_2);
        Output_3 = findViewById(R.id.Output_3);
        Holder_1 = findViewById(R.id.Holder_1);
        Holder_2 = findViewById(R.id.Holder_2);
        Holder_3 = findViewById(R.id.Holder_3);
        Holder_4 = findViewById(R.id.Holder_4);
        IBAtras = findViewById(R.id.IBAtras);
        IBPlay = findViewById(R.id.IBPlay);
        IBAdelante = findViewById(R.id.IBAdelante);
        IBAyuda = findViewById(R.id.IBAyuda);
        IBSonido = findViewById(R.id.IBSonido);
        //Colores
        color_caja_letra = getResources().getColor(R.color.caja_letra);
        color_caja_numero = getResources().getColor(R.color.caja_numero);
        color_fondo_juego = getResources().getColor(R.color.fondo_juego);
        color_color_error = getResources().getColor(R.color.color_error);
        //Posiciones de todos los views
        IBAdelante.setVisibility(View.INVISIBLE);
        IBAtras.setVisibility(View.INVISIBLE);
        Colocar();
        x=0;
        num_intentos=0;
        CElemento=0;
        CSElemento=0;
        CInstruccion=0;
        iJump=0;
        JUMPA=JUMPB=JUMPC=JUMPD=0;
        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
        setFlags();
        EstadoMusica=true;
        Jugando=false;
        init();
        if (savedInstanceState == null) {
            showFragment(BoardFragment.newInstance());
        }
        Ayuda(1000);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    public void IniciarElementos(){

        Log.i("Entrada", "Estos son los valores: "+nivel_actual.getInput());
        Entrada.addAll(Arrays.asList((nivel_actual.getInput()).split("-")));
        Resultado.addAll(Arrays.asList((nivel_actual.getOutput()).split("-")));
        for(int y=0; y<Salida.size(); y++){
            Salida.set(y, "");
        }
        Problema = nivel_actual.getProblema();
        CargarInputs();
        CargarOutputs();
    }

    private void init(){
        // Iniciar la musica y ponerla en Loop
        musica = MediaPlayer.create(getApplicationContext(), R.raw.musica);
        musica.setLooping(true);
        musica.start();
        // Con el id que le pasamos desde la parte de niveles, hacemos una busqueda que cargaremos en la pantalla.
        int id_nivel = getIntent().getExtras().getInt("id");
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        nivel_actual = BDAprender.getNivelDAO().getNivelPorID(id_nivel);

        Salida = new ArrayList<String>(0);
        Instrucciones = new ArrayList<String>(0);
        Resultado = new ArrayList<String>(0);
        Entrada = new ArrayList<String>(0);
        IniciarElementos();
        CargarInputs();
    }


    public void IBPlay(View v) {
        //TODO COMPROBAR SI HAY INSTRUCCIONES
        //TODO LIMPIAR DISPLAY
        //TODO LIMPIAR LOS HOLDERS
        if(!Jugando){
            if(InstruccionesString!=null){
                CElemento=0;
                CSElemento=0;
                // TODO Métodos de contains para saber las posiciones de los A B C y D
                A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                A.Desaparecer_Objeto(Holder_1);
                A.Desaparecer_Objeto(Holder_2);
                A.Desaparecer_Objeto(Holder_3);
                A.Desaparecer_Objeto(Holder_4);

                Instrucciones=  Arrays.asList(InstruccionesString.split("-"));
                Salida.clear();
                Jugando=true;
                IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                CargarOutputs();
                CargarInputs();
                x =0;
                // A Partir de aqui, nada cambia (Cuidado con los hilos. pueden romper el juego entero)
                JUMPA=JUMPB=JUMPC=JUMPD=0;
                CInstruccion=0;
                Log.i("for", "Lista entera: "+Instrucciones);
                for(final String instruccion : Instrucciones){
                    if(instruccion.equals("A")){
                        JUMPA=CInstruccion;
                    }
                    if(instruccion.equals("B")){
                        JUMPB=CInstruccion;
                    }
                    if(instruccion.equals("C")){
                        JUMPC=CInstruccion;
                    }
                    if(instruccion.equals("D")){
                        JUMPD=CInstruccion;
                    }
                    CInstruccion++;
                }
                CInstruccion=0;
                iJump=0;
                // Pasar a una lista las instrucciones.
                // Repetir hasta que no haya inputs
                while(CInstruccion<Instrucciones.size()){
                    Log.i("while", "Recorre la lista con isntrucción: "+Instrucciones.get(CInstruccion));
                    if(Instrucciones.get(CInstruccion).contains("jump")){
                        Log.i("while", "Jump indice: "+CInstruccion);
                        Log.i("while", "ijump: "+iJump);
                        iJump++;
                        if(nivel_actual.getIdNivel()==6 && iJump==3){
                            break;
                        }
                    }
                    if(iJump<Resultado.size()){
                        if(Instrucciones.get(CInstruccion).startsWith(" ")){
                            DetectorElementos(Instrucciones.get(CInstruccion).substring(1));
                        }else{
                            DetectorElementos(Instrucciones.get(CInstruccion));
                        }
                    }
                    CInstruccion++;
                    Log.i("while", "Lista con indice: "+CInstruccion);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Jugando=false;
                        IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                        num_intentos++;
                        ComrobarResultado();
                    }
                }, x);
            }else{
                Toast toast = Toast.makeText(this, "La lista de instrucciones esta vacía.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            //IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
            //Colocar();
        }
    }

    public void IBSonido(View v) {
        Sonido();
    }
    public void Sonido(){
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
    }


    public void Ayuda(int t){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Juego.this, ayuda.class);
                intent.putExtra("id",nivel_actual.getIdNivel());
                Juego.this.startActivity(intent);
            }
        }, t);
    }
    public void IBAyuda(View v) {
        // TODO Mostrar un cardview con la informacion del nivel y explicaciones de los distintos elementos
        Ayuda(10);
    }

    public void IBAtras(View v){
        //TODO Mover hacia atras en la lista de elementos
    }


    public void IBAdelante(View v){
        //TODO Mover hacia adelante en la lista de elementos
    }

    public void CargarInputs(){
        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
        if(CElemento<=Entrada.size()-1){
            A.Mostrar_Objeto(Input_1, Entrada.get(CElemento));
        }else{
            A.Desaparecer_Objeto(Input_1);
        }
        if(CElemento+1<=Entrada.size()-1){
            A.Mostrar_Objeto(Input_2, Entrada.get(CElemento+1));
        }else{
            A.Desaparecer_Objeto(Input_2);
        }
        if(CElemento+2<=Entrada.size()-1){
            A.Mostrar_Objeto(Input_3, Entrada.get(CElemento+2));
        }else{
            A.Desaparecer_Objeto(Input_3);
        }
    }

    public void CargarOutputs(){
        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
        Log.i("COut",CSElemento+"");
        Log.i("COut",Salida+"");
        if(CSElemento-1>=0) {
            if(Salida.get(CSElemento-1).equals("")){
                A.Desaparecer_Objeto(Output_1);
            }else{
                A.Mostrar_Objeto(Output_1, Salida.get(CSElemento-1));
            }
        }else{
            A.Desaparecer_Objeto(Output_1);
        }
        if(CSElemento-2>=0){
            if(Salida.get(CSElemento-2).equals("")){
                A.Desaparecer_Objeto(Output_2);
            }else{
                A.Mostrar_Objeto(Output_2, Salida.get(CSElemento-2));
            }
        }else{
            A.Desaparecer_Objeto(Output_2);
        }
        if(CSElemento-3>=0) {
            if (Salida.get(CSElemento-3).equals("")) {
                A.Desaparecer_Objeto(Output_3);
            } else {
                A.Mostrar_Objeto(Output_3, Salida.get(CSElemento-3));
            }
        }else{
            A.Desaparecer_Objeto(Output_3);
        }


    }



    public void DetectorElementos(String Instruccion) {
        Handler handler = new Handler();
        if (Instruccion.equals("input") || Instruccion.equals("output") || Instruccion.equals("bump+") || Instruccion.equals("bump-")) {
            switch (Instruccion) {
                case "input":
                    Input();
                    break;
                case "output":
                    Output();
                    break;
                case "bump+":
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            BumpMas();
                        }
                    }, x);
                    x=x+300;
                    break;
                case "bump-":
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            BumpMenos();
                        }
                    }, x);
                    x=x+300;
                    break;
            }
        }else{
                if(Instruccion.contains(" ")){
                    String[] InstruccionesArray = Instruccion.split(" ");
                    // TODO mirar porque cunado es un copyfrom le añade un espacio al principio
                    switch (InstruccionesArray[0]) {
                        case "copyto":
                            Log.i("Instruccion",InstruccionesArray[1]+" CT");
                            copyTo(InstruccionesArray[InstruccionesArray.length-1]);
                            break;
                        case "copyfrom":
                            Log.i("Instruccion",InstruccionesArray[1]+" CF");
                            copyFrom(InstruccionesArray[InstruccionesArray.length-1]);
                            break;
                        case "sum":
                            Sum(InstruccionesArray[InstruccionesArray.length-1]);
                            break;
                        case "sub":
                            Sub(InstruccionesArray[InstruccionesArray.length-1]);
                            break;
                        case "jump":
                            // Controlar el valor de jump
                            Jump(InstruccionesArray[InstruccionesArray.length-1]);
                            break;
                        default:
                            Log.i("Instruccion",InstruccionesArray[0]+" "+InstruccionesArray[1]+" DEFAULT");
                            break;
                    }
                }else{
                    switch (Instruccion){
                        //TODO Asignar a una variable el valor de la posicion.
                        case "A":
                            Log.i("Instruccion","Pasando por A");
                            break;
                        case "B":
                            Log.i("Instruccion","Pasando por B");
                            break;
                        case "C":
                            Log.i("Instruccion","Pasando por C");
                            break;
                        case "D":
                            Log.i("Instruccion","Pasando por D");
                            break;
                    }
                }
        }
    }


    public void Input(){
        // todas tienen sus propias cosas.
        Handler handler = new Handler();
        Log.i("Input 1", "Tiempo: "+x);
        handler.postDelayed(new Runnable() {
            public void run() {
                A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                A.MoverYVolver(Input_3, Input_2);
                A.MoverYVolver(Input_2, Input_1);
                A.MoverYVolver(Input_1, Actual);
            }
        }, x);
        x=x+510;
        Log.i("Input 2", "Tiempo: "+x);
        handler.postDelayed(new Runnable() {
            public void run() {
                A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                A.Mostrar_Objeto(Actual, Entrada.get(CElemento));
                Actual_Valor=Actual.getText().toString();
                CElemento++;
                CargarInputs();

            }
        }, x);
        x=x+100;
    }

    private void Output() {
        Log.i("Output 1", "Tiempo: "+x);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                A.MoverYVolver(Actual, Output_1);
                A.MoverYVolver(Output_1, Output_2);
                A.MoverYVolver(Output_2, Output_3);
            }
        }, x);
        x=x+510;
        Log.i("Output 2", "Tiempo: "+x);
        handler.postDelayed(new Runnable() {
            public void run() {
                A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                Salida.add(Actual_Valor);
                Actual_Valor="";
                CSElemento++;
                CargarOutputs();
                A.Desaparecer_Objeto(Actual);
            }
        }, x);
        x=x+100;
    }

    // Suma 1 al valor del cuadro, y le hace zoom (al cuadro) por unos milisegundos , para mostrar que está ocurriendo algo.
    private void BumpMas() {
        if(Character.isLetter(Actual_Valor.charAt(0))){
            Toast toast = Toast.makeText(this, "Error, esto es una letra", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
            Actual_Valor = Integer.toString(Integer.parseInt(Actual_Valor)+1);
            A.Mostrar_Objeto(Actual, Actual_Valor);
            A.ZoomObjetomas(Actual);
        }

    }

    private void BumpMenos() {
        if(Character.isLetter(Actual_Valor.charAt(0))){
            Toast toast = Toast.makeText(this, "Error, esto es una letra", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
            Actual_Valor = Integer.toString(Integer.parseInt(Actual_Valor) - 1);
            A.Mostrar_Objeto(Actual, Actual_Valor);
            A.ZoomObjetomenos(Actual);
        }
    }


    // mueve el objeto a uno de los holders, y lo rellena con el cuadro y el valor (cambia el fondo del cuadrado.), y vuelve a la posición inicial
    public void copyTo(final String holder){
        // TODO Añadir para comprobar si hay algo en el Actual.
        Handler handler = new Handler();
        Log.i("CopyTo1", "Tiempo: "+x);
        switch(holder){
            case "1":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Actual, Holder_1);
                    }
                }, x);
                x=x+510;
                Log.i("Copyto2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Holder_1, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "2":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Actual, Holder_2);
                    }
                }, x);
                x=x+510;
                Log.i("Copyto2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Holder_2, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "3":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Actual, Holder_3);
                    }
                }, x);
                x=x+510;
                Log.i("Copyto2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Holder_3, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "4":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Actual, Holder_4);
                    }
                }, x);
                x=x+510;
                Log.i("Copyto2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Holder_4, Actual_Valor);
                    }
                }, x);
                x=x+100;
                break;
        }
    }
    // mueve el objeto al holder y cambia el valor del objeto al contenido del holder, y vuelve a la posición inicio
    public void copyFrom(String holder){
        // TODO Añadir para comprobar si hay algo en el holder.
        Handler handler = new Handler();
        Log.i("CopyFrom1", "Tiempo: "+x);
        switch(holder){
            case "1":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_1, Actual);
                    }
                }, x);
                x=x+510;
                Log.i("CopyFrom2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Holder_1.getText().toString();
                        A.Mostrar_Objeto(Actual, Actual_Valor);

                    }
                }, x);
                x=x+100;

                break;
            case "2":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_2, Actual);
                    }
                }, x);
                x=x+510;
                Log.i("CopyFrom2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Actual, Holder_2.getText().toString());
                        Actual_Valor = Actual.getText().toString();
                    }
                }, x);
                x=x+100;

                break;
            case "3":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_3, Actual);
                    }
                }, x);
                x=x+510;
                Log.i("CopyFrom2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Actual, Holder_3.getText().toString());
                        Actual_Valor = Actual.getText().toString();
                    }
                }, x);
                x=x+100;

                break;
            case "4":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_4, Actual);
                    }
                }, x);
                x=x+510;
                Log.i("CopyFrom2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.Mostrar_Objeto(Actual, Holder_4.getText().toString());
                        Actual_Valor = Actual.getText().toString();
                    }
                }, x);
                x=x+100;
                break;
        }

    }
    public void Sum(String holder){
        Handler handler = new Handler();
        Log.i("Sum1", "Tiempo: "+x);
        switch(holder){
            case "1":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_1,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sum2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Holder_1.getText().toString()) + Integer.parseInt(Actual.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;
                break;
            case "2":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_2,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sum2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Holder_2.getText().toString()) + Integer.parseInt(Actual.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "3":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_3,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sum2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Holder_3.getText().toString()) + Integer.parseInt(Actual.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "4":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_4,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sum2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Holder_4.getText().toString()) + Integer.parseInt(Actual.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;
                break;
        }
    }
    // El objeto de la zona de inicio, va al holder marcado, hace la resta, el resultado se queda en el objeto y vuelve al inicio
    public void Sub(String holder){
        Handler handler = new Handler();
        Log.i("Sub1", "Tiempo: "+x);
        switch(holder){
            case "1":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_1,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sub2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Actual.getText().toString()) - Integer.parseInt(Holder_1.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;
                break;
            case "2":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_2,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sub2", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Actual.getText().toString()) - Integer.parseInt(Holder_2.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "3":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_3,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sub3", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Actual.getText().toString()) - Integer.parseInt(Holder_3.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;

                break;
            case "4":
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        A.MoverYVolver(Holder_4,Actual);
                    }
                }, x);
                x=x+510;
                Log.i("Sub4", "Tiempo: "+x);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        A = new Animaciones(color_caja_letra, color_caja_numero, color_fondo_juego, color_color_error);
                        Actual_Valor = Integer.toString(Integer.parseInt(Actual.getText().toString()) - Integer.parseInt(Holder_4.getText().toString()));
                        A.Mostrar_Objeto(Actual, Actual_Valor);
                    }
                }, x);
                x=x+100;
                break;
        }
    }
    public void Jump(String Lugar){
        // Nos va a dar la letra.

        switch (Lugar){
            //TODO Asignar a una variable el valor de la posicion.
            case "A":
                Log.i("Instruccion","Saltando por A");
                CInstruccion=JUMPA;
                break;
            case "B":
                Log.i("Instruccion","Saltando por B");
                CInstruccion=JUMPB;
                break;
            case "C":
                Log.i("Instruccion","Saltando por C");
                CInstruccion=JUMPC;
                break;
            case "D":
                Log.i("Instruccion","Saltando por D");
                CInstruccion=JUMPD;
                break;
        }

        // Tiene que repetir, tantas veces como lo necesite, osea que mirar el resultado, con un if o algo, por lo que si no es necesario, no hace el salto

        // TODO Jumps basados en cambiar el valor de CElemento no es CINstrucción, por lo que necesita uno nuevo
        //Jump
        //Jump if zero
        //Jump if negative
        //Jump if not number
    }




    public void ComrobarResultado(){

        Log.i("Salida", ""+Salida);
        Log.i("Resultado", ""+Resultado);

        if(Salida.equals(Resultado)){
            Toast toast = Toast.makeText(this, "Good Job, has necesitado "+num_intentos+" intentos", Toast.LENGTH_SHORT);
            toast.show();
            Victoria();

        }else{
            Toast toast = Toast.makeText(this, "NO es la solucion, llevas: "+num_intentos+" intentos", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    // Si mientras se ejecuta, ocurre algún error en la ejecución, con las reglas, te lo mostraría con este método, hace que ele elemento se ponga en rojo en la lista.
    public void ErrorEnEjecución(){

    }

    // Muestra la calificación, en un cardview y la guarda en la base de datos.
    public void Victoria(){
        //INSERTAR DATOS DE LA PARTIDA
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        googleUser = GoogleSignIn.getLastSignedInAccount(this);
        String uid ="";

        //ver si se ha iniciado sesion con google o con firebase
        if(firebaseUser != null || googleUser != null) {
            //inicio de sesion con firebase
            if(firebaseUser != null) {
                uid = firebaseUser.getUid();
            }else{
                //inicio de sesion con google
                uid = googleUser.getId();
            }

            DatabaseReference lvlmaxRef = myRef.child(uid + "/lvlmax");
            DatabaseReference puntuacionesRef = myRef.child(uid + "/puntuaciones/lvl" + nivel_actual.getIdNivel());

            if(num_intentos==1){
                lvlmaxRef.setValue(nivel_actual.getIdNivel());
                puntuacionesRef.setValue(3);
            }
            if(num_intentos==2 || num_intentos==3){
                lvlmaxRef.setValue(nivel_actual.getIdNivel());
                puntuacionesRef.setValue(2);
            }
            if(num_intentos>3){
                lvlmaxRef.setValue(nivel_actual.getIdNivel());
                puntuacionesRef.setValue(1);
            }
        }else{
            Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
            if(num_intentos==1){
                Solucion solucion = new Solucion(nivel_actual.getIdNivel(), 3);
                BDAprender.getSolucionDAO().insert(solucion);
            }
            if(num_intentos==2 || num_intentos==3){
                Solucion solucion = new Solucion(nivel_actual.getIdNivel(), 2);
                BDAprender.getSolucionDAO().insert(solucion);
            }
            if(num_intentos>3){
                Solucion solucion = new Solucion(nivel_actual.getIdNivel(), 1);
                BDAprender.getSolucionDAO().insert(solucion);
            }
            //TODO No se ha iniciado sesion ni con firebase ni con google. Guardar la puntuacion en la base de datos local
        }

        //MOSTRAR DIALOGO
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Juego.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_result, null);
        Button mSiguiente = (Button)mView.findViewById(R.id.victory_next);
        Button mVolver = (Button)mView.findViewById(R.id.victory_back);
        ImageView star1 = (ImageView)mView.findViewById(R.id.star1);
        ImageView star2 = (ImageView)mView.findViewById(R.id.star2);
        ImageView star3 = (ImageView)mView.findViewById(R.id.star3);

        if(num_intentos==1){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star);
        }
        if(num_intentos==2 || num_intentos==3){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.empty_star);
        }
        if(num_intentos>3){
            star1.setImageResource(R.drawable.star);
            star2.setImageResource(R.drawable.empty_star);
            star3.setImageResource(R.drawable.empty_star);
        }

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        mSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AÑADIR INTENT AL SIGUIENTE NIVEL
                //TODO startActivity(new Intent(MainActivity.this, MenuNivelActivity.class));
                Intent intent = new Intent(Juego.this, LoadGame.class);
                intent.putExtra("id",nivel_actual.getIdNivel()+1);
                Juego.this.startActivity(intent);
                Toast.makeText(Juego.this, "Siguiente nivel", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        mVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CAMBIAR EL CONTEXT POR EL DEL JUEGO
                //startActivity(new Intent(Juego.this, MenuNivelActivity.class));
                dialog.dismiss();
                finish();
            }
        });

        //IR AL MENU DE NIVELES CUANDO SE DA A LA FLECHITA DE ATRAS DEL MOVIL
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                //startActivity(new Intent(Juego.this, MenuNivelActivity.class));
                finish();
            }
        });

        dialog.show();
    }

    // Método para colocar de nuevo todos los elementos
    public void Colocar(){
        Log.i("Posiciones de input 1", IX+" "+IO1Y+"");
        Input_1.setX(IX);
        Input_1.setY(IO1Y);
        Log.i("Posiciones de input 1", IX+" "+IO2Y+"");
        Input_2.setX(IX);
        Input_2.setY(IO2Y);
        Input_3.setX(IX);
        Input_3.setY(IO3Y);

        Actual.setX(AX);
        Actual.setY(AY);

        Output_1.setX(OX);
        Output_1.setY(IO1Y);
        Output_2.setX(OX);
        Output_2.setY(IO2Y);
        Output_3.setX(OX);
        Output_3.setY(IO3Y);


        Holder_1.setX(HIX);
        Holder_1.setY(HAY);
        Holder_2.setX(HDX);
        Holder_2.setY(HAY);
        Holder_3.setX(HIX);
        Holder_3.setY(HDY);
        Holder_4.setX(HDX);
        Holder_4.setY(HDY);

    }

    // Hacer que la cadena de instrucciones se pase a esta clase.
    public void getValores(String Cadena){
        //Por si la cadena esta vacía y solo tiene un -
        if(Cadena.length()>1){
            InstruccionesString=Cadena.substring(1);
        }
    }

    // Pantalla Completa
    public void setFlags() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    //Para los botones de abajo, para que pare la cancion
    @Override
    public void onBackPressed() {
        musica.pause();
        finish();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(musica.isPlaying())
            Sonido();

        else
            return;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(musica.isPlaying())
            Sonido();
        else
            return;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setFlags();
        if(musica.isPlaying())
            return;
        else
            Sonido();

    }

}
