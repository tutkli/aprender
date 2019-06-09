package com.example.clara.aprender;

import androidx.room.Room;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    DatabaseReference myRef = database.getReference("usuarios");

    GridLayout mainGrid;
    CardView cardView1, cardView2, cardView4;
    ImageView btn_config;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    TextView currentUser;
    GoogleSignInAccount googleUser;
    FirebaseUser firebaseUser;
    static final int RC_SIGN_IN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(firebaseUser, googleUser);
        firebaseUser = mAuth.getCurrentUser();
        googleUser = GoogleSignIn.getLastSignedInAccount(this);

        currentUsername();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_action, null);
        TextView mTitulo = (TextView) mView.findViewById(R.id.action_title);
        TextView mMsg = (TextView) mView.findViewById(R.id.action_msg);
        Button mCancelar = (Button) mView.findViewById(R.id.btn_action_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.btn_action_aceptar);

        mTitulo.setText("Cerrar Aplicación");
        mMsg.setText("¿Seguro que quieres cerrar la aplicación?");

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlags();
                dialog.dismiss();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        dialog.show();
    }






    @Override
    public void onResume(){
        super.onResume();

        setFlags();
    }

    @SuppressWarnings("ConstantConditions")
    public void currentUsername() {
        currentUser = (TextView)findViewById(R.id.current_user);
        currentUser.setText("");

        if(googleUser != null){
            String personName = googleUser.getDisplayName();
            currentUser.setText(personName);
        }

        if(firebaseUser != null){
            myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String personName = dataSnapshot.child("nombre").getValue().toString();
                        currentUser.setText(personName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        //JUGAR
        cardView1 = (CardView)findViewById(R.id.cardView1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MenuNivelActivity.class);
                startActivity(i);
            }
        });

        //PUNTUACION
        cardView2 = (CardView)findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PuntuacionActivity.class);
                startActivity(i);
            }
        });

        //CONFIGURACIÓN
        btn_config = (ImageView) findViewById(R.id.btn_config);
        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ConfiguracionActivity.class);
                startActivity(i);
            }
        });

        //CREDITOS
        cardView4 = (CardView)findViewById(R.id.cardView4);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreditosActivity.class);
                startActivity(i);
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setFlags();

        //Insertar datos, al final con el main thread. Ya que si la base es pequeña no imprta.
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();

        //Insertamos unos niveles para comprobar
        Nivel nivel = new Nivel(1, "1. Entrar y Salir", "input-input-input-output-output-output", "Haz que todos los elementos de la cola de entrada terminen en la cola de salida.",
                "6-5-4", "6-5-4");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(2, "2. Repitiendo Tareas", "input-output-jump A-A", "Envía todos los elementos a la salida. Esta vez con la instruccion de Jump.",
                "A-B-D-S-G-U-E-S-D-C-G-D-W-A-A", "A-B-D-S-G-U-E-S-D-C-G-D-W-A-A");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(3, "3. Copiando elementos", "input-input-input-copyto 1- copyfrom 1-output-output-output-output-output", "Coge los elementos de la lista input y escribe ERROR, mediante el uso de copyto.",
                "E-R-O", "E-R-R-O-R");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(4, "4. Mezclador", "input-input-output-output-copyto 1-copyfrom 1-jump A-A", "Coge los dos primeros objetos del input y envíalos al revés, repite hasta que la cola esté vacía. ",
                "3-5-N-A-4-6", "5-3-A-N-6-4");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(5, "Nivel 5", "input-input-output-copyto 0-add 0-jump A-A", "Por cada 2 elementos, sumalos y entrega el resultado.", "2-6-1-3-2-4-5-4", "8-4-6-8");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(6, "Nivel 6", "input-output-copyto 0-sum 0-sum 0-jump A-A", "Por cada Input, envialo triplicado", "5-7-5-0-2", "15-21-15-0-6");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(7, "Nivel 7", "input-output-jump A-A-jump if 0 B-B", "Envia solo los objetos que no sean zero.", "3-0-2-A-D-0-0-2", "3-2-A-D-2");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(8, "Nivel 8", "input-output-jump A-A-jump B-B-jump if 0 C-C", "Envía solo los 0.", "A-2-0-1-0-2-G-0-A-0", "0-0-0-0");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(9, "Nivel 9", "input-output-jump A-A-jump B-B-jump if 0 C-C", "Envía solo los 0.", "A-2-0-1-0-2-G-0-A-0", "0-0-0-0");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(10, "Nivel 10", "input-output-jump A-A-jump B-B-jump if 0 C-C", "Envía solo los 0.", "1-2-3-4", "1-2-3-4");
        BDAprender.getNivelDAO().insert(nivel);

    }

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


}