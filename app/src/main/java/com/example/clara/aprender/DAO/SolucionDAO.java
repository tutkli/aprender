package com.example.clara.aprender.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.clara.aprender.Modelos.Solucion;
import java.util.List;

@Dao
public interface SolucionDAO {
    @Insert
    void insert(Solucion... Soluciones);

    @Update
    void update(Solucion... Soluciones);

    @Delete
    void delete(Solucion... Soluciones);

    @Query("SELECT * FROM solucion")
    List<Solucion> getSoluciones();

    @Query("SELECT * FROM solucion WHERE IdSolucion = :idsolucion")
    Solucion getSolucionPorID(int idsolucion);
}
