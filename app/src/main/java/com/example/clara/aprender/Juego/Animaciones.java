package com.example.clara.aprender.Juego;

import android.widget.TextView;
import com.example.clara.aprender.R;

public class Animaciones extends Juego{
    public int caja_letra = getResources().getColor(R.color.caja_letra);
    int caja_numero = getResources().getColor(R.color.caja_numero);
    // Cambiar las animaciones y lo necesario para la representacion visual aquí.

    // Métodos más básicos a utilizar Mostrar el Objeto y Ocultarlo
    public void Mostrar_Objeto(TextView txt){
        txt.setBackgroundColor(DetectarColores((txt.getText().toString())));
    }

    public void Desaparecer_Objeto(TextView txt){
        txt.setText("");
        txt.setBackgroundColor(getResources().getColor(R.color.fondo_juego));
    }

    public int DetectarColores(String Valor) {
        if(Character.isLetter(Valor.charAt(0))){
            return caja_letra;
        }else{
            return caja_numero;
        }
    }

    // Métodos para mover los elementos


}
