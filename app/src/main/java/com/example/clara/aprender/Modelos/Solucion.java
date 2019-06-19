package com.example.clara.aprender.Modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "solucion")
public class Solucion {
    @PrimaryKey
    private int IdSolucion;
    private int Puntuacion;


    public Solucion(){
    }

    public Solucion(int IdSolucion, int Puntuacion){
        this.IdSolucion = IdSolucion;
        this.Puntuacion = Puntuacion;
    }


    public int getIdSolucion() {return IdSolucion;}
    public void setIdSolucion(int idSolucion) {this.IdSolucion = idSolucion;}

    public int getPuntuacion() {return Puntuacion;}
    public void setPuntuacion(int Puntuacion) {this.Puntuacion = Puntuacion;}
}
