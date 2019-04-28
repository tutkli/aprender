package com.example.clara.aprender.Base_datos;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.example.clara.aprender.DAO.NivelDAO;
import com.example.clara.aprender.DAO.SolucionDAO;
import com.example.clara.aprender.DAO.UsuarioDAO;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.Modelos.Solucion;
import com.example.clara.aprender.Modelos.Usuario;


@Database(entities = {Nivel.class, Solucion.class, Usuario.class}, version=1)
public abstract class Base_datos_Aprender extends RoomDatabase {
    public abstract NivelDAO getNivelDAO();
    public abstract SolucionDAO getSolucionDAO();
    public abstract UsuarioDAO getUsuarioDAO();

}

/*
Para instanciar la base de datos
AppDatabase BDAprender = Room.databaseBuilder(getApplicationContext(), Base_datos_Aprender.class, "base_datos_aprender").allowMainThreadQueries().build()
//Permite a Room hacer operaciones en el hilo principal----------------------------------------------------^
//Añadir para que los hilos se ejecuten en background
*/