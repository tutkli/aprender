package com.example.clara.aprender.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.clara.aprender.Modelos.Nivel;
import java.util.List;

//Varargs
@Dao
public interface NivelDAO {

    // Que era ... ? recordar y añadir al archivo word

    // OnConflictStrategy consiste en no insertar si ocurre algo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Nivel... Niveles);

    @Update
    void update(Nivel... Niveles);

    @Delete
    void delete(Nivel... Niveles);

    @Query("SELECT * FROM nivel")
    List<Nivel> getNiveles();

    @Query("SELECT * FROM nivel WHERE IdNivel = :idnivel")
    Nivel getNivelPorID(int idnivel);
}
