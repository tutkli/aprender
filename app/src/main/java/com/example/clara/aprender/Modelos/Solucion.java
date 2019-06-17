package com.example.clara.aprender.Modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "solucion")
public class Solucion {
    private String Solucion;
    private int IdNivel;
    @PrimaryKey
    private int IdSolucion;

    public Solucion(){
    }

    public Solucion(int IdNivel, int IdUsuario, int IdSolucion, String Solucion){
        this.IdNivel = IdNivel;
        this.IdSolucion = IdSolucion;
        this.Solucion = Solucion;
    }


    public int getIdNivel() {return IdNivel;}
    public void setIdNivel(int idNivel) {this.IdNivel = idNivel;}

    public int getIdSolucion() {return IdSolucion;}
    public void setIdSolucion(int idSolucion) {this.IdSolucion = idSolucion;}

    public String getSolucion() {return Solucion;}
    public void setSolucion(String solucion) { this.Solucion = solucion;}

}
