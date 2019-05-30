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
        //FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);

        //updateUI(firebaseUser, googleUser);
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

    private void updateUI(FirebaseUser firebaseUser, GoogleSignInAccount googleUser) {
        /*-------- Check if user is already logged in or not--------*/
        if (firebaseUser != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(!firebaseUser.isEmailVerified()) {
                Toast.makeText(MainActivity.this, "Verifica tu email antes de continuar.", Toast.LENGTH_SHORT).show();
                login();
            }
        }else{
            if(googleUser != null){
                Toast.makeText(MainActivity.this, "INICIO DE SESION DE GOOGLE", Toast.LENGTH_SHORT).show();
            }else{
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View mView = layoutInflater.inflate(R.layout.dialog_bienvenido, null);
                Button mLogin = (Button) mView.findViewById(R.id.btn_login);
                Button mSignUp = (Button) mView.findViewById(R.id.btn_signUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK){
                            Toast.makeText(MainActivity.this, "Inicia sesión para continuar", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                });

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
    }


    public void login(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_login, null);
        final EditText mEmail = (EditText) mView.findViewById(R.id.login_email);
        final EditText mPassword = (EditText) mView.findViewById(R.id.login_password);
        TextView mRecuperar = (TextView) mView.findViewById(R.id.login_recuperar_password);
        Button mCancelar = (Button) mView.findViewById(R.id.login_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.login_aceptar);
        SignInButton mGoogle = (SignInButton) mView.findViewById(R.id.sign_in_button);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    Toast.makeText(MainActivity.this, "Inicia sesión para continuar", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        mRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                if(email.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduce un email de recuperación", Toast.LENGTH_SHORT).show();
                }else {
                    recuperarPassword(email);
                }
            }
        });

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
                                        Log.d("FirebaseLogin:", "signInWithEmail:success");
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        updateUI(firebaseUser, null);
                                        Toast.makeText(MainActivity.this, "Se ha iniciado sesión correctamente.", Toast.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login:", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Fallo en la autentificación.", Toast.LENGTH_SHORT).show();
                                        updateUI(null, null);

                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

        mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
                dialog.dismiss();
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

                if(!username.trim().isEmpty() && !email.trim().isEmpty() && checkPassword(password.trim())) {
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
                                        updateUI(null, null);

                                        dialog.dismiss();
                                    }

                                }
                            });
                }else {
                    Toast.makeText(MainActivity.this, "Introduce todos los datos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    private void googleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        if (completedTask!=null){
            try {
                googleUser = completedTask.getResult(ApiException.class);

                //AQUI COGE EL EMAIL, Y EL USER Y LLAMA AL METODO DE GUARDAR, Y FALLA
                String personName = googleUser.getDisplayName();
                String personEmail = googleUser.getEmail();
                String personId = googleUser.getId();

                insertarUser(personId, personName, personEmail);

                // Signed in successfully, show authenticated UI.
                updateUI(null, googleUser);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("GoogleLogin", "signInResult:failed code=" + e.getStatusCode());
                updateUI(null, null);
            }
        }else{
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            updateUI(firebaseUser, null);
        }

        currentUsername();
    }

    public boolean checkPassword(String password) {
        if(password.length() < 6){
            Toast.makeText(MainActivity.this, "La contraseña debe tener 6 o más caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    public void recuperarPassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Se ha enviado un correo a su email.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @SuppressWarnings("ConstantConditions")
    public void currentUsername() {
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);
        if(googleUser != null){
            currentUser = (TextView)findViewById(R.id.current_user);
            String personName = googleUser.getDisplayName();
            currentUser.setText(personName);
        }

        if(mAuth.getCurrentUser()!=null){
            FirebaseUser currentusuario = mAuth.getCurrentUser();
            currentUser = (TextView)findViewById(R.id.current_user);
            myRef.child( currentusuario.getUid()).addValueEventListener(new ValueEventListener() {
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