package com.example.clara.aprender.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
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
