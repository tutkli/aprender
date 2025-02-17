package com.example.clara.aprender;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");
    DatabaseReference myRef = database.getReference("usuarios");
    private EditText emailc,passc,etdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailc=(EditText) findViewById(R.id.editText3);
        passc=(EditText) findViewById(R.id.editText2);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void registrarse(View view){

        AlertDialog.Builder dialog   = new AlertDialog.Builder(Login.this);
        dialog.setTitle("nombre de usuario");
        dialog.setMessage("Introduzca el nombre de usuario");
        etdialog = new EditText(this);
        dialog.setView(etdialog);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String usuario = etdialog.getText().toString();
                if(!emailc.getText().toString().isEmpty()){
                 reg(usuario);
                }else{
                    Toast.makeText(Login.this, "escriba el nombre de usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    public void insertar(String uid,String  usuario) {
        String email = emailc.getText().toString();
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
public void reg(final String usuario){
    String email=emailc.getText().toString();
    String password=passc.getText().toString();
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SplashActivity:", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        insertar(uid,usuario);
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "verificacion de email enviada",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SplashActivity:", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                }
            });
}
    public void login(View view){
        String email=emailc.getText().toString();
        String password=passc.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SplashActivity:", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SplashActivity:", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        /*-------- Check if user is already logged in or not--------*/
        if (user != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(user.isEmailVerified()){
                Toast.makeText(Login.this, "Ya has iniciado sesion .",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,MainActivity.class));
            }
            else {
                Toast.makeText(Login.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void recuperarcon(View view) {
        String email = emailc.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        if(!emailc.getText().toString().isEmpty()){
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "correo de cambio de contraseña enviado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(Login.this, "escriba el email de recuperacion", Toast.LENGTH_LONG).show();
        }
    }
}
