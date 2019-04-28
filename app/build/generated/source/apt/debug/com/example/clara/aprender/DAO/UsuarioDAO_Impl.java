package com.example.clara.aprender.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.clara.aprender.Modelos.Usuario;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class UsuarioDAO_Impl implements UsuarioDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfUsuario;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfUsuario;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfUsuario;

  public UsuarioDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUsuario = new EntityInsertionAdapter<Usuario>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `usuario`(`Nombre`,`IdUsuario`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Usuario value) {
        if (value.getNombre() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getNombre());
        }
        stmt.bindLong(2, value.getIdUsuario());
      }
    };
    this.__deletionAdapterOfUsuario = new EntityDeletionOrUpdateAdapter<Usuario>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `usuario` WHERE `IdUsuario` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Usuario value) {
        stmt.bindLong(1, value.getIdUsuario());
      }
    };
    this.__updateAdapterOfUsuario = new EntityDeletionOrUpdateAdapter<Usuario>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `usuario` SET `Nombre` = ?,`IdUsuario` = ? WHERE `IdUsuario` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Usuario value) {
        if (value.getNombre() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getNombre());
        }
        stmt.bindLong(2, value.getIdUsuario());
        stmt.bindLong(3, value.getIdUsuario());
      }
    };
  }

  @Override
  public void insert(Usuario... Usuarios) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfUsuario.insert(Usuarios);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Usuario... Usuarios) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfUsuario.handleMultiple(Usuarios);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Usuario... Usuarios) {
    __db.beginTransaction();
    try {
      __updateAdapterOfUsuario.handleMultiple(Usuarios);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Usuario> getUsuarios() {
    final String _sql = "SELECT * FROM usuario";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfNombre = _cursor.getColumnIndexOrThrow("Nombre");
      final int _cursorIndexOfIdUsuario = _cursor.getColumnIndexOrThrow("IdUsuario");
      final List<Usuario> _result = new ArrayList<Usuario>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Usuario _item;
        _item = new Usuario();
        final String _tmpNombre;
        _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
        _item.setNombre(_tmpNombre);
        final int _tmpIdUsuario;
        _tmpIdUsuario = _cursor.getInt(_cursorIndexOfIdUsuario);
        _item.setIdUsuario(_tmpIdUsuario);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Usuario getUsuarioPorID(int idusuario) {
    final String _sql = "SELECT * FROM usuario WHERE IdUsuario = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idusuario);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfNombre = _cursor.getColumnIndexOrThrow("Nombre");
      final int _cursorIndexOfIdUsuario = _cursor.getColumnIndexOrThrow("IdUsuario");
      final Usuario _result;
      if(_cursor.moveToFirst()) {
        _result = new Usuario();
        final String _tmpNombre;
        _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
        _result.setNombre(_tmpNombre);
        final int _tmpIdUsuario;
        _tmpIdUsuario = _cursor.getInt(_cursorIndexOfIdUsuario);
        _result.setIdUsuario(_tmpIdUsuario);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int TablaTamano() {
    final String _sql = "SELECT count(*) FROM usuario";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
