package com.example.clara.aprender.Modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "nivel")
public class Nivel {
    @PrimaryKey
    private int idNivel;
    private String titulo;
    private String instrucciones;
    private String problema;
    private String input;
    private String output;
    private boolean resuelto;

    public Nivel(){
    }

    public Nivel(int idNivel, String titulo, String instrucciones, String problema, String input, String output, boolean resuelto) {
        this.idNivel = idNivel;
        this.titulo = titulo; // Título de cada problema
        this.instrucciones = instrucciones; // La cadena de elementos
        this.problema = problema; // Enunciado del problema
        this.input = input; // Cadena de elementos Input
        this.output = output; // Cadena de elementos Output
        this.resuelto = resuelto; // Si está hecho o no.
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

    public String getIntrucciones() {
        return instrucciones;
    }
    public void setIntrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
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
