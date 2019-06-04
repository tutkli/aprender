package com.example.clara.aprender.Juego;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.MenuNivelActivity;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class Juego extends AppCompatActivity {

    ImageButton IBSonido, IBAyuda, IBAtras, IBPlay, IBAdelante;
    TextView Output_1, Output_2, Output_3, Input_1, Input_2, Input_3, Actual;
    boolean EstadoMusica;
    MediaPlayer musica;
    static Nivel nivel_actual;
    boolean juego_start;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    DatabaseReference myRef = database.getReference("usuarios");
    FirebaseUser firebaseUser;
    GoogleSignInAccount googleUser;
    // Porque una lista y no un array? Me gustan las listas.
    List<String> Entrada, Salida;
    int Contador_Entrada, Contador_Salida, Contador_Inicial_Entrada, Contador_Inicial_Salida, Contador_Veces_Play;

    //Valores para el juego
    String Objeto, Output_ini, Input_ini, Instrucciones, Problema, Valor_actual;
    int ContadorInstruccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        setFlags();
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

    public void IniciarElementos(){
        // Elementos de entrada
        Input_ini = nivel_actual.getInput();
        Entrada = Arrays.asList(Input_ini.split("-"));
        Contador_Inicial_Entrada = Entrada.size();

        // Elemenentos de Salida
        Output_ini = nivel_actual.getOutput();
        Salida = Arrays.asList(Output_ini.split("-"));
        Contador_Inicial_Salida = Salida.size();

        // Cargar el enunciado del problema
        Problema = nivel_actual.getProblema();
        ContadorInstruccion=0;
        Contador_Entrada=0;
        Contador_Salida=0;
    }

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
        //Mostrar un cardview con la informacion del nivel y explicaciones de los distintos elementos
    }
    //Ir cada 0.5 segundos una instruccion hacia adelante
    //Tiene que coger los elementos de la columna 2, y lo tiene que comparar con una solucion?
    private void Jugar(){
        // Recorrer la lista
        // Poner un switch, que según el elemento de la lista, haga distintas cosas.
        //Iniciamos los elementos
        IniciarElementos();
        IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));

        //De esta manera conseguimos tener las instrucciones para ser traducidas.
        List<String> InstruccionesArray = Arrays.asList(Instrucciones.split("-"));
        for(String Instruccion : InstruccionesArray){
            DetectorElementos(Instruccion);
        }
        Contador_Veces_Play++;
        //Poner para que la cadena se recorte por uno, porque en el inicio hay un guión que sobra.
    }
    //Cuando esta ejecutandose, el boton de play se cambia a parar, donde podemos parar el juego, el index de instrucciones se reinicia y se cambia el boton a play
    private void Parar(){
        // Poner el estado de restart
        // Cambiar todos los elementos a color normal
        // Reiniciar el contador
        // Reiniciar las animaciones
        IBPlay.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        IniciarElementos();

    }

    public int DetectarColores(String Valor) {
        int color;
        if(Character.isLetter(Valor.charAt(0))){
            color = getResources().getColor(R.color.caja_letra);
        }else{
            color = getResources().getColor(R.color.caja_numero);
        }
        return color;
    }

    //Mover hacia atrás en la lista de elementos
    private void Atras(){

    }
    //Mover hacia adelante en la lista de elementos
    private void Adelante(){

    }

    private void init(){
        //Iniciar los elementos del constraint layout
        Input_1 = findViewById(R.id.Input_1);
        Input_2 = findViewById(R.id.Input_2);
        Input_3 = findViewById(R.id.Input_3);
        Output_1 = findViewById(R.id.Output_1);
        Output_2 = findViewById(R.id.Output_2);
        Output_3 = findViewById(R.id.Output_3);
        Actual = findViewById(R.id.Actual);

        // Iniciar la musica y ponerla en Loop
        musica = MediaPlayer.create(getApplicationContext(), R.raw.musica);
        musica.setLooping(true);
        musica.start();
        //Inicializar los botones de la barra de abajo
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
                Objeto="";
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
        IniciarElementos();
        Inputs();
        Outputs();
    }

    public void Inputs() {
        // Cargar los valores del input en las cajas de los inputs.
        if(Entrada.size()>0){
            Input_1.setText(Entrada.get(Contador_Entrada));
            Input_1.setBackgroundColor(DetectarColores(Entrada.get(Contador_Entrada)));
        }else{
            Input_1.setText("");
            Input_1.setBackgroundColor(getResources().getColor(R.color.fondo_juego));
        }
        if(Entrada.size()>1){
            Input_2.setText(Entrada.get(Contador_Entrada+1));
            Input_2.setBackgroundColor(DetectarColores(Entrada.get(Contador_Entrada+1)));
        }else{
            Input_2.setText("");
            Input_2.setBackgroundColor(getResources().getColor(R.color.fondo_juego));
        }
        if(Entrada.size()>2) {
            Input_3.setText(Entrada.get(Contador_Entrada+2));
            Input_3.setBackgroundColor(DetectarColores(Entrada.get(Contador_Entrada+2)));
        }else{
            Input_3.setText("");
            Input_3.setBackgroundColor(getResources().getColor(R.color.fondo_juego));
        }
    }

    public void Outputs(){
        // Cargar los valores del input en las cajas de los inputs.
        if(Salida.size()>0){
            Output_1.setText(Salida.get(Contador_Salida));
            Output_1.setBackgroundColor(DetectarColores(Salida.get(Contador_Salida)));
        }else{
            Output_1.setText("");
            Output_1.setBackgroundColor(DetectarColores(Salida.get(Contador_Salida)));
        }
        if(Salida.size()>1){
            Output_2.setText(Salida.get(Contador_Salida+1));
            Output_2.setBackgroundColor(DetectarColores(Salida.get(Contador_Salida+1)));
        }else{

        }
        if(Salida.size()>2) {
            Output_3.setText(Salida.get(Contador_Salida+2));
            Output_3.setBackgroundColor(DetectarColores(Salida.get(Contador_Salida+2)));
        }else{

        }

    }

    public void DetectarColor(String Valor){

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

    //Para las animaciones:

    public void AnimacionInput(){
        Actual.getLeft();
        Actual.getTop();
        ObjectAnimator animation = ObjectAnimator.ofFloat(Input_1, "translationX", Actual.getLeft(), Actual.getTop());
        animation.setDuration(500);
        animation.start();
    }

    public void AnimacionOutput(){
        Output_1.getLeft();
        Output_1.getTop();
        ObjectAnimator animation = ObjectAnimator.ofFloat(Actual, "translationX", Output_1.getLeft(), Output_1.getTop());
        animation.setDuration(500);
        animation.start();
    }




    // Métodos del juego.
    // El objeto es un cuadro.
    // Inicio: es el estado por defecto, a lo que vuelve la caja.

    public void Input(){
        Inputs();
        AnimacionInput();
        Valor_actual=Entrada.get(Contador_Entrada);
        Contador_Entrada++;

    }
    // Mueve la caja de inicio, que necesita un valor dentro y lo mueve a la parte de output, y elimina el valor de la caja, y vuelve al inicio
    public void Output(){
        Outputs();
        AnimacionOutput();
        Input_1.setText(Valor_actual);
        Valor_actual="";

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
        //INSERTAR DATOS DE LA PARTIDA
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        googleUser = GoogleSignIn.getLastSignedInAccount(this);
        String uid ="";
        int puntuacion, numeroDelNivel;
        puntuacion=numeroDelNivel=0;

        //ver si se ha iniciado sesion con google o con firebase
        if(firebaseUser != null || googleUser != null) {
            //inicio de sesion con firebase
            if(firebaseUser != null) {
                uid = firebaseUser.getUid();
            }else{
             //inicio de sesion con google
                uid = googleUser.getId();
            }

            //guardar puntuaciones
            DatabaseReference lvlmaxRef = myRef.child(uid + "/lvlmax");
            lvlmaxRef.setValue(numeroDelNivel);
            //ACTUALIZAR LA PUNTUACION DEL NIVEL
            DatabaseReference puntuacionesRef = myRef.child(uid + "/puntuaciones/lvl" + numeroDelNivel);
            puntuacionesRef.setValue(puntuacion);

        }else{
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

        //SEGUN LA PUNTUACION, CAMBIAR LA IMAGEN DE LA ESTRELLA (POR EJEMPLO, SE PUEDE PONER LA IMAGEN POR DEFECTO LA ESTRELLA VACIA Y CAMBIAR LA IMAGEN CON UN SWITCH)
        star1.setImageResource(R.drawable.star);
        star2.setImageResource(R.drawable.empty_star);
        star3.setImageResource(R.drawable.empty_star);

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
                //AÑADIR INTENT AL SIGUIENTE NIVEL
                //startActivity(new Intent(MainActivity.this, MenuNivelActivity.class));
                Toast.makeText(Juego.this, "Siguiente nivel", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        mVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CAMBIAR EL CONTEXT POR EL DEL JUEGO
                startActivity(new Intent(Juego.this, MenuNivelActivity.class));
                dialog.dismiss();
            }
        });

        //IR AL MENU DE NIVELES CUANDO SE DA A LA FLECHITA DE ATRAS DEL MOVIL
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                startActivity(new Intent(Juego.this, MenuNivelActivity.class));
            }
        });

        dialog.show();
    }

    public void getValores(String Cadena){
        //Por si la cadena esta vacía y solo tiene un -
        if(Cadena.length()>1){
            Instrucciones=Cadena.substring(1);
        }
    }


    //Para eliminar la barra de abajo y hacerlo en pantalla completa
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
        if(musica.isPlaying())
            return;
        else
            Sonido();

    }

}
