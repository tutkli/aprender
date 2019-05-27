package com.example.clara.aprender;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfiguracionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

    public void cerrarSesion(View v){
        FirebaseAuth.getInstance().signOut();

        Toast.makeText(ConfiguracionActivity.this,"Se ha cerrado sesión correctamente",Toast.LENGTH_LONG).show();

        startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
        finish();
    }

    public void borrarUser(View v){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfiguracionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_action, null);
        TextView mTitulo = (TextView) mView.findViewById(R.id.action_title);
        TextView mMsg = (TextView) mView.findViewById(R.id.action_msg);
        Button mCancelar = (Button) mView.findViewById(R.id.btn_action_cancelar);
        Button mAceptar = (Button) mView.findViewById(R.id.btn_action_aceptar);

        mTitulo.setText("Borrar Usuario");
        mMsg.setText("Si acepta su cuenta será completamente eliminada y perderá todos los datos");

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
                FirebaseUser currentUser = mAuth.getCurrentUser();
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ConfiguracionActivity.this,"Su cuenta ha sido eliminada",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
                        }else{
                            Toast.makeText(ConfiguracionActivity.this,"Error al eliminar cuenta",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    public void init() {
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
