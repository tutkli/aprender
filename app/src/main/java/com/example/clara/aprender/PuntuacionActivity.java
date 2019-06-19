package com.example.clara.aprender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.example.clara.aprender.Adapters.puntuacion_adapter;
import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;

import java.util.List;

public class PuntuacionActivity extends AppCompatActivity {

    List<Nivel> lstLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        init();
        // TODO, cargar las puntuacStr@ken14
        
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

        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        lstLevel = BDAprender.getNivelDAO().getNiveles();

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_view);
        puntuacion_adapter myAdapter = new puntuacion_adapter(this,lstLevel);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);
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