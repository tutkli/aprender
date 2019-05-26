package com.example.clara.aprender;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    DatabaseReference myRef = database.getReference("usuarios");

    GridLayout mainGrid;
    CardView cardView1, cardView2, cardView4;
    ImageView btn_config;

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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    private void updateUI(FirebaseUser CurrentUser) {
        /*-------- Check if user is already logged in or not--------*/
        if (CurrentUser != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(!CurrentUser.isEmailVerified()){
                Toast.makeText(MainActivity.this, "Verifica tu email antes de continuar.", Toast.LENGTH_SHORT).show();
                login();
            }
        }else{
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mView = layoutInflater.inflate(R.layout.dialog_bienvenido, null);
            Button mLogin = (Button) mView.findViewById(R.id.btn_login);
            Button mSignUp = (Button) mView.findViewById(R.id.btn_signUp);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(false);

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                    dialog.dismiss();

                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp();
                    dialog.dismiss();

                }
            });

            dialog.show();
        }
    }

    public void login(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_login, null);
        final EditText mEmail = (EditText) mView.findViewById(R.id.login_email);
        final EditText mPassword = (EditText) mView.findViewById(R.id.login_password);
        Button mCancelar = (Button) mView.findViewById(R.id.login_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.login_aceptar);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                dialog.dismiss();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.trim().isEmpty() || password.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduce email y contraseña", Toast.LENGTH_SHORT).show();
                }else {

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login:", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Toast.makeText(MainActivity.this, "Se ha iniciado sesión correctamente.", Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Login:", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Fallo en la autentificación.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);

                                    dialog.dismiss();
                                }
                            }
                        });
                }
            }
        });

        dialog.show();

    }

    public void signUp(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_signup, null);
        final EditText mUsername = (EditText) mView.findViewById(R.id.signup_username);
        final EditText mEmail = (EditText) mView.findViewById(R.id.signup_email);
        final EditText mPassword = (EditText) mView.findViewById(R.id.signup_password);
        Button mCancelar = (Button) mView.findViewById(R.id.signup_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.signup_aceptar);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                dialog.dismiss();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString();
                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!username.trim().isEmpty() && !email.trim().isEmpty() && !password.trim().isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SignUp:", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        insertarUser(uid,username, email);
                                        user.sendEmailVerification();
                                        //Toast.makeText(MainActivity.this, "Verificación de email enviada.", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                        dialog.dismiss();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SignUp:", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Fallo en la autentificación.", Toast.LENGTH_SHORT).show();
                                        updateUI(null);

                                        dialog.dismiss();
                                    }

                                }
                            });
                }
            }
        });

        dialog.show();
    }

    public void insertarUser(String uid,String  usuario, String email) {
        DatabaseReference emailRef = myRef.child(uid + "/email");
        emailRef.setValue(email);
        DatabaseReference userRef = myRef.child(uid + "/nombre");
        userRef.setValue(usuario);
        DatabaseReference lvlmaxRef = myRef.child(uid + "/lvlmax");
        lvlmaxRef.setValue("0");
        for (int cont = 1; cont < 11; cont++) {
            DatabaseReference puntuacionesRef = myRef.child(uid + "/puntuaciones/lvl" + cont);
            puntuacionesRef.setValue("0");
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

        setFlags();

        //Insertar datos, al final con el main thread. Ya que si la base es pequeña no imprta.
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();

        //Insertamos un nivel para comprobar
        Nivel nivel = new Nivel(1, "Nivel 1", 5, "Desplaza del input al output", "1-2-3-4", "1-2-3-4", false);
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(2, "Nivel 2", 7, "Desplaza del input al output", "1-2-3-4", "1-2-3-4", false);
        BDAprender.getNivelDAO().insert(nivel);
        nivel = new Nivel(3, "Nivel 3", 9, "Desplaza del input al output", "1-2-3-4", "1-2-3-4", false);
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