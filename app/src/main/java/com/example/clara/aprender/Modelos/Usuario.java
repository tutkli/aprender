package com.example.clara.aprender.Modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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
