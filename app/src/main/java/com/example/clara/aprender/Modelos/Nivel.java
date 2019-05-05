package com.example.clara.aprender.Modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "nivel")
public class Nivel {
    private String titulo;
    private int espacios;
    private String resultado;
    private int solucion;
    @PrimaryKey
    private int idNivel;

    public Nivel(){
    }

    public Nivel(String titulo, int espacios, String resultado, int solucion, int idNivel) {
        this.titulo = titulo;
        this.espacios = espacios;
        this.resultado = resultado;
        this.solucion = solucion;
        this.idNivel = idNivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEspacios() {
        return espacios;
    }

    public void setEspacios(int espacios) {
        this.espacios = espacios;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getSolucion() {
        return solucion;
    }

    public void setSolucion(int solucion) {
        this.solucion = solucion;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }
}
