package com.example.clara.aprender.Juego;

import android.widget.TextView;
import com.github.florent37.viewanimator.ViewAnimator;

public class Animaciones{
    public int color_caja_letra;
    public int color_caja_numero;
    public int color_fondo_juego;
    public int color_color_error;



    public Animaciones(int caja_letra, int caja_numero, int fondo_juego, int color_error ){
        color_caja_letra = caja_letra;
        color_caja_numero = caja_numero;
        color_fondo_juego = fondo_juego;
        color_color_error = color_error;



    }


    public void MoverYVolver(TextView Inicio, TextView Objetivo){
        float X = Inicio.getX();
        float Y = Inicio.getY();
        float OX = Objetivo.getX();
        float OY = Objetivo.getY();
        ViewAnimator
                .animate(Inicio)
                .translationY(Y, OY)
                .dp().translationX(X, OX)
                .duration(500)
                .thenAnimate(Inicio)
                .translationY(OY, Y)
                .translationX(OX, X)
                .duration(1)
                .start();
    }
    public void ZoomObjeto(TextView Objeto){
        ViewAnimator
                .animate(Objeto)
                .scale(1f, 0.5f, 1f)
                .duration(190)
                .start();
    }

    public void Mostrar_Objeto(TextView txt, String valor){
        txt.setText(valor);
        txt.setBackgroundColor(DetectarColores((txt.getText().toString())));
    }

    public void Desaparecer_Objeto(TextView txt){
        txt.setText("");
        txt.setBackgroundColor(color_fondo_juego);
    }

    public int DetectarColores(String Valor) {
        if(Character.isLetter(Valor.charAt(0))){
            return color_caja_letra;
        }else{
            return color_caja_numero;
        }
    }
}
