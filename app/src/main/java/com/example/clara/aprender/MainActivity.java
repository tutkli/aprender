package com.example.clara.aprender;

import androidx.room.Room;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
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
import com.tooltip.Tooltip;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if(firebaseUser == null && googleUser == null) {
            showTooltip(btn_config, Gravity.BOTTOM);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

    public void showTooltip(View v, int gravity) {
        ImageButton btn = (ImageButton)v;
        Tooltip tooltip = new Tooltip.Builder(btn)
                .setText("¡Inicia sesión para guardar tu progreso!")
                .setTextColor(Color.WHITE)
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setDismissOnClick(true)
                .show();
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

        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(),
                Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();

        //Insertamos unos niveles para comprobar
        Nivel nivel = new Nivel(1, "1. Pasar Elementos", "input-input-input-output-output-output",
                "Haz que todos los elementos de la cola de entrada terminen en la cola de salida.",
                "6-5-4", "6-5-4");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(2, "2. Repitiendo Tareas", "input-output-jump A-A",
                "Envía todos los elementos a la salida. Esta vez con la instruccion de Jump.",
                "A-B-D-S-G-U-E-S-D-C-G-D-W-A-A", "A-B-D-S-G-U-E-S-D-C-G-D-W-A-A");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(3, "3. Copiando elementos", "input-input-input-output-output-output-output-output-copyto 1-copyfrom 1-copyfrom 1",
                "Coge los elementos de la lista input y escribe ERROR, mediante el uso de copyto.",
                "E-R-O", "E-R-R-O-R");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(4, "4. Sumando 1", "input-output-jump A-A-bump+", "Suma 1 a todos los números.", "2-5-4-7-4-5-3-2", "3-6-5-8-5-6-4-3");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(5, "5. Restando 1", "input-output-jump A-A-bump-", "Resta 1 a todos los números.", "2-5-4-7-4-5-3-2", "1-4-3-6-3-4-2-1");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(6, "6. Mezclador", "input-input-output-output-copyto 1-copyfrom 1-jump A-A",
                "Coge los dos primeros objetos del input y envíalos al revés, repite hasta que la cola esté vacía. ",
                "3-5-N-A-4-6", "5-3-A-N-6-4");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(7, "7. Sumando Elementos", "input-input-output-copyto 1-sum 1-jump A-A", "Por cada 2 elementos, sumalos y entrega el resultado.", "2-6-1-3-2-4-5-4", "8-4-6-9");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(8, "8. Restando Elementos", "input-input-output-copyto 1-sub 1-jump A-A", "Por cada 2 elementos, resta al primero es segundo y entrega el resultado.", "8-1-5-2-9-3-2-1", "7-3-6-1");
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(9, "9. Triplicando", "input-output-copyto 1-sum 1-sum 1-jump A-A", "Por cada Input, envialo triplicado", "5-7-5-0-2", "15-21-15-0-6");
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