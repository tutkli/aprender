package com.example.clara.aprender.Juego.Ayuda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.example.clara.aprender.Adapters.RecyclerViewAdapter;
import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.R;

import java.util.List;

public class ayuda extends AppCompatActivity {
    Nivel nivel_actual;

    @Override
    public void onResume(){
        super.onResume();
        setFlags();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        init();
    }

    public void volver(View v){
        finish();
    }

    //INICIALIZACION DE ITEMS
    private void init(){
        int id_nivel = getIntent().getExtras().getInt("id");
        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        nivel_actual = BDAprender.getNivelDAO().getNivelPorID(id_nivel);
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recycler_view);
        ayuda_adapter myAdapter = new ayuda_adapter(this,nivel_actual);
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
