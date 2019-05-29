package com.example.clara.aprender;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        mAuth = FirebaseAuth.getInstance();
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

    public void cerrarSesion(View v){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            FirebaseAuth.getInstance().signOut();
        }else{
            mGoogleSignInClient.signOut();
        }
        Toast.makeText(ConfiguracionActivity.this, "Se ha cerrado sesión correctamente", Toast.LENGTH_LONG).show();
        startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
        finish();

    }

    public void borrarUser(View view){
        AlertDialog.Builder dialog   = new AlertDialog.Builder(ConfiguracionActivity.this);
        dialog.setTitle("Estas seguro?");
        dialog.setMessage("Si acepta su cuenta sera completamente borrada y perdera todos los datos");
        dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser!=null){
                    eliminaruser();
                    currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ConfiguracionActivity.this,"cuenta eliminada",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ConfiguracionActivity.this,MainActivity.class));
                            }else{
                                Toast.makeText(ConfiguracionActivity.this,"error al eliminar el usuario",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    eliminaruser();
                    mGoogleSignInClient.revokeAccess() .addOnCompleteListener (new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ConfiguracionActivity.this,"cuenta eliminada",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ConfiguracionActivity.this,MainActivity.class));
                            }else{
                                Toast.makeText(ConfiguracionActivity.this,"error al eliminar el usuario",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }}
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
    public void eliminaruser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            DatabaseReference uidRef = myRef.child(currentUser.getUid());
            uidRef.removeValue();
        }else{
            GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);
            DatabaseReference uidRef = myRef.child(googleUser.getEmail());
            uidRef.removeValue();
        }
    }
    public void init() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        setFlags();

        Switch aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(ConfiguracionActivity.this, "ENABLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfiguracionActivity.this, "DISABLED", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
