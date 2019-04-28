package com.example.clara.aprender.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.clara.aprender.Modelos.Usuario;
import java.util.List;


@Dao
public interface UsuarioDAO {
    @Insert
    void insert(Usuario... Usuarios);

    @Update
    void update(Usuario... Usuarios);

    @Delete
    void delete(Usuario... Usuarios);

    @Query("SELECT * FROM usuario")
    List<Usuario> getUsuarios();

    @Query("SELECT * FROM usuario WHERE IdUsuario = :idusuario")
    Usuario getUsuarioPorID(int idusuario);

    @Query("SELECT count(*) FROM usuario")
    int TablaTamano();
}
