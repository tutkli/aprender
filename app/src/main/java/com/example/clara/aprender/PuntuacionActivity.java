package com.example.clara.aprender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PuntuacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

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

    public void init() {
        setFlags();
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