package com.example.clara.aprender;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracionActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    DatabaseReference myRef = database.getReference("usuarios");
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser firebaseUser;
    GoogleSignInAccount googleUser;
    static final int RC_SIGN_IN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        init();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        setFlags();
    }

    public void volver(View v){
        finish();
    }

    public void iniciarSesion(View v) {
        updateUI(firebaseUser, googleUser);
    }

    public void cerrarSesion(View v){
        if(firebaseUser!=null || googleUser != null){
            if (firebaseUser!=null) {
                FirebaseAuth.getInstance().signOut();
            }else {
                mGoogleSignInClient.signOut();
            }

            Toast.makeText(ConfiguracionActivity.this, "Se ha cerrado sesión correctamente", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(ConfiguracionActivity.this, "No se ha iniciado sesión", Toast.LENGTH_LONG).show();
        }
    }

    public void borrarUser(View view){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfiguracionActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) ConfiguracionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_action, null);
        TextView mTitulo = (TextView) mView.findViewById(R.id.action_title);
        TextView mMsg = (TextView) mView.findViewById(R.id.action_msg);
        Button mCancelar = (Button) mView.findViewById(R.id.btn_action_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.btn_action_aceptar);

        mTitulo.setText("Borrar Usuario");
        mMsg.setText("Si acepta su cuenta sera completamente borrada y perdera todos los datos");


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setFlags();

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null || googleUser != null) {
                    eliminarUser();
                } else{
                    Toast.makeText(ConfiguracionActivity.this, "Inicia sesión para borrar tu usuario", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    public void eliminarUser(){
        if(firebaseUser != null) {
            DatabaseReference uidRef = myRef.child(firebaseUser.getUid());
            uidRef.removeValue();

            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ConfiguracionActivity.this, "Se ha eliminado el usuario correctamente", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ConfiguracionActivity.this, "Error al eliminar usuario", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            DatabaseReference uidRef = myRef.child(googleUser.getId());
            uidRef.removeValue();

            mGoogleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ConfiguracionActivity.this, "Se ha eliminado el usuario correctamente", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ConfiguracionActivity.this, "Error al eliminar usuario", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void updateUI(FirebaseUser firebaseUser, GoogleSignInAccount googleUser) {
        /*-------- Check if user is already logged in or not--------*/
        if (firebaseUser != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(!firebaseUser.isEmailVerified()) {
                Toast.makeText(ConfiguracionActivity.this, "Verifica tu email antes de continuar", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ConfiguracionActivity.this, "Se ha iniciado sesión correctamente con un email", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            if(googleUser != null){
                Toast.makeText(ConfiguracionActivity.this, "Se ha iniciado sesión correctamente con Google", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfiguracionActivity.this);
                LayoutInflater layoutInflater = (LayoutInflater) ConfiguracionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View mView = layoutInflater.inflate(R.layout.dialog_bienvenido, null);
                Button mLogin = (Button) mView.findViewById(R.id.btn_login);
                Button mSignUp = (Button) mView.findViewById(R.id.btn_signUp);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                setFlags();

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

    public void signUp(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfiguracionActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) ConfiguracionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_signup, null);
        final EditText mUsername = (EditText) mView.findViewById(R.id.signup_username);
        final EditText mEmail = (EditText) mView.findViewById(R.id.signup_email);
        final EditText mPassword = (EditText) mView.findViewById(R.id.signup_password);
        Button mCancelar = (Button) mView.findViewById(R.id.signup_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.signup_aceptar);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setFlags();

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            .addOnCompleteListener(ConfiguracionActivity.this, new OnCompleteListener<AuthResult>() {
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

                                        updateUI(user, null);

                                        dialog.dismiss();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SignUp:", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(ConfiguracionActivity.this, "Fallo en la autentificación.", Toast.LENGTH_SHORT).show();
                                        updateUI(null, null);

                                        dialog.dismiss();
                                    }

                                }
                            });
                }else {
                    Toast.makeText(ConfiguracionActivity.this, "Introduce todos los datos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public boolean checkPassword(String password) {
        if(password.length() < 6){
            Toast.makeText(ConfiguracionActivity.this, "La contraseña debe tener 6 o más caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void login(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfiguracionActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) ConfiguracionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mView = layoutInflater.inflate(R.layout.dialog_login, null);
        final EditText mEmail = (EditText) mView.findViewById(R.id.login_email);
        final EditText mPassword = (EditText) mView.findViewById(R.id.login_password);
        TextView mRecuperar = (TextView) mView.findViewById(R.id.login_recuperar_password);
        Button mCancelar = (Button) mView.findViewById(R.id.login_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.login_aceptar);
        SignInButton mGoogle = (SignInButton) mView.findViewById(R.id.sign_in_button);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setFlags();

        mRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                if(email.trim().isEmpty()) {
                    Toast.makeText(ConfiguracionActivity.this, "Introduce un email de recuperación", Toast.LENGTH_SHORT).show();
                }else {
                    recuperarPassword(email);
                }
            }
        });

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.trim().isEmpty() || password.trim().isEmpty()) {
                    Toast.makeText(ConfiguracionActivity.this, "Introduce email y contraseña", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ConfiguracionActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("FirebaseLogin:", "signInWithEmail:success");
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        updateUI(firebaseUser, null);

                                        dialog.dismiss();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login:", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(ConfiguracionActivity.this, "Fallo en la autentificación.", Toast.LENGTH_SHORT).show();
                                        //updateUI(null, null);

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
    }

    public void insertarUser(String uid,String  usuario, String email) {
        DatabaseReference emailRef = myRef.child(uid + "/email");
        emailRef.setValue(email);
        DatabaseReference userRef = myRef.child(uid + "/nombre");
        userRef.setValue(usuario);
        DatabaseReference lvlmaxRef = myRef.child(uid + "/lvlmax");
        lvlmaxRef.setValue("0");
        for (int cont = 1; cont < 11; cont++) {
            //TODO Guardar las puntuaciones actuales en la base de datos si se ha jugado sin iniciar sesion
            DatabaseReference puntuacionesRef = myRef.child(uid + "/puntuaciones/lvl" + cont);
            puntuacionesRef.setValue("0");
        }
    }

    public void recuperarPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ConfiguracionActivity.this, "Se ha enviado un correo a su email.", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ConfiguracionActivity.this, "Wack", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setFlags();

        firebaseUser = mAuth.getCurrentUser();
        googleUser = GoogleSignIn.getLastSignedInAccount(this);
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
