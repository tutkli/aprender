package com.example.clara.aprender.Modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "solucion")
public class Solucion {
    private String Solucion;
    private int IdUsuario;
    private int IdNivel;
    @PrimaryKey
    private int IdSolucion;

    public Solucion(){
    }

    public Solucion(int IdNivel, int IdUsuario, int IdSolucion, String Solucion){
        this.IdNivel = IdNivel;
        this.IdUsuario = IdUsuario;
        this.IdSolucion = IdSolucion;
        this.Solucion = Solucion;
    }


    public int getIdUsuario() {return IdUsuario;}
    public void setIdUsuario(int idUsuario) {this.IdUsuario = idUsuario;}

    public int getIdNivel() {return IdNivel;}
    public void setIdNivel(int idNivel) {this.IdNivel = idNivel;}

    public int getIdSolucion() {return IdSolucion;}
    public void setIdSolucion(int idSolucion) {this.IdSolucion = idSolucion;}

    public String getSolucion() {return Solucion;}
    public void setSolucion(String solucion) { this.Solucion = solucion;}
}
