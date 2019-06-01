package com.example.clara.aprender.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
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
