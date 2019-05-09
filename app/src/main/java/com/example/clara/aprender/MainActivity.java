package com.example.clara.aprender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private GridLayout mainGrid;
    private CardView cardView1, cardView2, cardView3, cardView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //INICIALIZACIÓN DE ITEMS
    private void init() {
        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        cardView1 = (CardView)findViewById(R.id.cardView1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(MainActivity.this, "CLICK EN JUGAR", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, MenuNivelActivity.class);
                startActivity(i);
            }
        });

        //EN CADA LISTENER SE PONE EL INTENT A DONDE VAYA

        cardView2 = (CardView)findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CLICK EN PUNTUACIÓN", Toast.LENGTH_SHORT).show();
            }
        });

        cardView3 = (CardView)findViewById(R.id.cardView3);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(MainActivity.this, "CLICK EN CRÉDITOS", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, CreditosActivity.class);
                startActivity(i);
            }
        });
        cardView4 = (CardView)findViewById(R.id.cardView4);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CLICK EN CONFIGURACIÓN", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
