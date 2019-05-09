package com.example.clara.aprender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.clara.aprender.Modelos.Nivel;

import java.util.ArrayList;
import java.util.List;

public class MenuNivelActivity extends AppCompatActivity {


    List<Nivel> lstLevel;
    RecyclerViewAdapter adapter = null;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_nivel);

        init();
    }

    //INICIALIZACION DE ITEMS
    private void init(){

        lstLevel = new ArrayList<>();
        //creacion de 6 niveles
        for(int i=0;i<6;i++) {
            lstLevel.add(new Nivel("Nivel "+i, 10, "0", 34, i, R.drawable.star));
        }

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lstLevel);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}
