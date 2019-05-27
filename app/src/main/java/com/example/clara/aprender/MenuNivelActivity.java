package com.example.clara.aprender;

import androidx.room.Room;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.clara.aprender.Adapters.RecyclerViewAdapter;
import com.example.clara.aprender.Base_datos.Base_datos_Aprender;
import com.example.clara.aprender.Modelos.Nivel;

import java.util.List;

public class MenuNivelActivity extends AppCompatActivity {


    List<Nivel> lstLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_nivel);

        init();
    }

    @Override
    public void onResume(){
        super.onResume();

        setFlags();
    }

    public void volver(View v){
        finish();
    }

    //INICIALIZACION DE ITEMS
    private void init(){

        Base_datos_Aprender BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build();
        lstLevel = BDAprender.getNivelDAO().getNiveles();

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lstLevel);
        myrv.setLayoutManager(new GridLayoutManager(this,5));
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
