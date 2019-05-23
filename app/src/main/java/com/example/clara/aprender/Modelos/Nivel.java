package com.example.clara.aprender.Modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "nivel")
public class Nivel {
    @PrimaryKey
    private int idNivel;
    private String titulo;
    private int espacios;
    private String problema;
    private String input;
    private String output;
    private boolean resuelto;

    public Nivel(){
    }

    public Nivel(int idNivel, String titulo, int espacios, String problema, String input, String output, boolean resuelto) {
        this.idNivel = idNivel;
        this.titulo = titulo;
        this.espacios = espacios;
        this.problema = problema;
        this.input = input;
        this.output = output;
        this.resuelto = resuelto;
    }

    public int getIdNivel() {
        return idNivel;
    }
    public void setIdNivel(int idNivel) {
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

    public String getProblema() { return problema;}
    public void setProblema(String problema) { this.problema = problema;}

    public boolean getResuelto() {
        return resuelto;
    }
    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }
    public void setOutput(String output) {
        this.output = output;
    }
}
