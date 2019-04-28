package com.example.clara.aprender.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.clara.aprender.Modelos.Solucion;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SolucionDAO_Impl implements SolucionDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfSolucion;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfSolucion;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfSolucion;

  public SolucionDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSolucion = new EntityInsertionAdapter<Solucion>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `solucion`(`Solucion`,`IdUsuario`,`IdNivel`,`IdSolucion`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Solucion value) {
        if (value.getSolucion() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getSolucion());
        }
        stmt.bindLong(2, value.getIdUsuario());
        stmt.bindLong(3, value.getIdNivel());
        stmt.bindLong(4, value.getIdSolucion());
      }
    };
    this.__deletionAdapterOfSolucion = new EntityDeletionOrUpdateAdapter<Solucion>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `solucion` WHERE `IdSolucion` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Solucion value) {
        stmt.bindLong(1, value.getIdSolucion());
      }
    };
    this.__updateAdapterOfSolucion = new EntityDeletionOrUpdateAdapter<Solucion>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `solucion` SET `Solucion` = ?,`IdUsuario` = ?,`IdNivel` = ?,`IdSolucion` = ? WHERE `IdSolucion` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Solucion value) {
        if (value.getSolucion() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getSolucion());
        }
        stmt.bindLong(2, value.getIdUsuario());
        stmt.bindLong(3, value.getIdNivel());
        stmt.bindLong(4, value.getIdSolucion());
        stmt.bindLong(5, value.getIdSolucion());
      }
    };
  }

  @Override
  public void insert(Solucion... Soluciones) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfSolucion.insert(Soluciones);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Solucion... Soluciones) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfSolucion.handleMultiple(Soluciones);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Solucion... Soluciones) {
    __db.beginTransaction();
    try {
      __updateAdapterOfSolucion.handleMultiple(Soluciones);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Solucion> getSoluciones() {
    final String _sql = "SELECT * FROM solucion";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfSolucion = _cursor.getColumnIndexOrThrow("Solucion");
      final int _cursorIndexOfIdUsuario = _cursor.getColumnIndexOrThrow("IdUsuario");
      final int _cursorIndexOfIdNivel = _cursor.getColumnIndexOrThrow("IdNivel");
      final int _cursorIndexOfIdSolucion = _cursor.getColumnIndexOrThrow("IdSolucion");
      final List<Solucion> _result = new ArrayList<Solucion>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Solucion _item;
        _item = new Solucion();
        final String _tmpSolucion;
        _tmpSolucion = _cursor.getString(_cursorIndexOfSolucion);
        _item.setSolucion(_tmpSolucion);
        final int _tmpIdUsuario;
        _tmpIdUsuario = _cursor.getInt(_cursorIndexOfIdUsuario);
        _item.setIdUsuario(_tmpIdUsuario);
        final int _tmpIdNivel;
        _tmpIdNivel = _cursor.getInt(_cursorIndexOfIdNivel);
        _item.setIdNivel(_tmpIdNivel);
        final int _tmpIdSolucion;
        _tmpIdSolucion = _cursor.getInt(_cursorIndexOfIdSolucion);
        _item.setIdSolucion(_tmpIdSolucion);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Solucion getSolucionPorID(int idsolucion) {
    final String _sql = "SELECT * FROM solucion WHERE IdSolucion = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idsolucion);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfSolucion = _cursor.getColumnIndexOrThrow("Solucion");
      final int _cursorIndexOfIdUsuario = _cursor.getColumnIndexOrThrow("IdUsuario");
      final int _cursorIndexOfIdNivel = _cursor.getColumnIndexOrThrow("IdNivel");
      final int _cursorIndexOfIdSolucion = _cursor.getColumnIndexOrThrow("IdSolucion");
      final Solucion _result;
      if(_cursor.moveToFirst()) {
        _result = new Solucion();
        final String _tmpSolucion;
        _tmpSolucion = _cursor.getString(_cursorIndexOfSolucion);
        _result.setSolucion(_tmpSolucion);
        final int _tmpIdUsuario;
        _tmpIdUsuario = _cursor.getInt(_cursorIndexOfIdUsuario);
        _result.setIdUsuario(_tmpIdUsuario);
        final int _tmpIdNivel;
        _tmpIdNivel = _cursor.getInt(_cursorIndexOfIdNivel);
        _result.setIdNivel(_tmpIdNivel);
        final int _tmpIdSolucion;
        _tmpIdSolucion = _cursor.getInt(_cursorIndexOfIdSolucion);
        _result.setIdSolucion(_tmpIdSolucion);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
