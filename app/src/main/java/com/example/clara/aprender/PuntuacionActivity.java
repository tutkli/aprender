package com.example.clara.aprender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PuntuacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);
    }

    public void volver(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
