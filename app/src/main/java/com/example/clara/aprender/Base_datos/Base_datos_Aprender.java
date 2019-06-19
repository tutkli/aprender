package com.example.clara.aprender.Base_datos;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.clara.aprender.DAO.NivelDAO;
import com.example.clara.aprender.DAO.SolucionDAO;
import com.example.clara.aprender.Modelos.Nivel;
import com.example.clara.aprender.Modelos.Solucion;


@Database(entities = {Nivel.class, Solucion.class}, version=1)
public abstract class Base_datos_Aprender extends RoomDatabase {
    public abstract NivelDAO getNivelDAO();
    public abstract SolucionDAO getSolucionDAO();
}
