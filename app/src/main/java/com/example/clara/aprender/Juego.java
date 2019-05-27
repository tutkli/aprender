package com.example.clara.aprender;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.clara.aprender.Lista_Juego.BoardFragment;

public class Juego extends AppCompatActivity {

    ImageButton IBSonido, IBAyuda, IBAtras, IBPlay, IBAdelante;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        if (savedInstanceState == null) {
            showFragment(BoardFragment.newInstance());
        }
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    private void Sonido(){

    }

    private void Ayuda(){

    }
    private void Atras(){

    }
    private void Jugar(){

    }
    private void Parar(){

    }
    private void Adelante(){

    }
    //Como se hacen los botones?
    /*
    IBSonido.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, CreditosActivity.class);
            startActivity(i);
        }
    });
    */
}
