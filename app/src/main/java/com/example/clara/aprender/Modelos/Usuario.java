package com.example.clara.aprender.Modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//Si no pones tableName utiliza el de la clase
@Entity(tableName = "usuario")
public class Usuario {
    private String Nombre;
    @PrimaryKey
    private int IdUsuario;

    public int getIdUsuario() {return IdUsuario;}
    public void setIdUsuario(int idUsuario) {this.IdUsuario = idUsuario;}

    public String getNombre() {return Nombre;}
    public void setNombre(String nombre) {this.Nombre = nombre;}
}
