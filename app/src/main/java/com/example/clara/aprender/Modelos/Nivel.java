package com.example.clara.aprender.Modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "nivel")
public class Nivel {
    private String Titulo;
    private int Espacios;
    private String resultado;
    private int Solucion;
    @PrimaryKey
    private int IdNivel;


    public int getIdNivel() {return IdNivel;}
    public void setIdNivel(int idNivel) {this.IdNivel = idNivel;}

    public String getTitulo() {return Titulo;}
    public void setTitulo(String titulo) {this.Titulo = titulo;}

    public int getEspacios() {return Espacios;}
    public void setEspacios(int espacios) {this.Espacios = espacios;}

    public String getResultado() {return resultado;}
    public void setResultado(String resultado) {this.resultado = resultado;}

    public int getSolucion() {return Solucion;}
    public void setSolucion(int solucion) {Solucion = solucion;}


}
