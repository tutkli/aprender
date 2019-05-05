package com.example.clara.aprender.DAO;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.example.clara.aprender.Modelos.Nivel;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class NivelDAO_Impl implements NivelDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfNivel;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfNivel;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfNivel;

  public NivelDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNivel = new EntityInsertionAdapter<Nivel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `nivel`(`titulo`,`espacios`,`resultado`,`solucion`,`idNivel`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Nivel value) {
        if (value.getTitulo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitulo());
        }
        stmt.bindLong(2, value.getEspacios());
        if (value.getResultado() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getResultado());
        }
        stmt.bindLong(4, value.getSolucion());
        stmt.bindLong(5, value.getIdNivel());
      }
    };
    this.__deletionAdapterOfNivel = new EntityDeletionOrUpdateAdapter<Nivel>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `nivel` WHERE `idNivel` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Nivel value) {
        stmt.bindLong(1, value.getIdNivel());
      }
    };
    this.__updateAdapterOfNivel = new EntityDeletionOrUpdateAdapter<Nivel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `nivel` SET `titulo` = ?,`espacios` = ?,`resultado` = ?,`solucion` = ?,`idNivel` = ? WHERE `idNivel` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Nivel value) {
        if (value.getTitulo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitulo());
        }
        stmt.bindLong(2, value.getEspacios());
        if (value.getResultado() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getResultado());
        }
        stmt.bindLong(4, value.getSolucion());
        stmt.bindLong(5, value.getIdNivel());
        stmt.bindLong(6, value.getIdNivel());
      }
    };
  }

  @Override
  public void insert(Nivel... Niveles) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfNivel.insert(Niveles);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Nivel... Niveles) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfNivel.handleMultiple(Niveles);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Nivel... Niveles) {
    __db.beginTransaction();
    try {
      __updateAdapterOfNivel.handleMultiple(Niveles);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Nivel> getNiveles() {
    final String _sql = "SELECT * FROM nivel";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTitulo = _cursor.getColumnIndexOrThrow("titulo");
      final int _cursorIndexOfEspacios = _cursor.getColumnIndexOrThrow("espacios");
      final int _cursorIndexOfResultado = _cursor.getColumnIndexOrThrow("resultado");
      final int _cursorIndexOfSolucion = _cursor.getColumnIndexOrThrow("solucion");
      final int _cursorIndexOfIdNivel = _cursor.getColumnIndexOrThrow("idNivel");
      final List<Nivel> _result = new ArrayList<Nivel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Nivel _item;
        _item = new Nivel();
        final String _tmpTitulo;
        _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
        _item.setTitulo(_tmpTitulo);
        final int _tmpEspacios;
        _tmpEspacios = _cursor.getInt(_cursorIndexOfEspacios);
        _item.setEspacios(_tmpEspacios);
        final String _tmpResultado;
        _tmpResultado = _cursor.getString(_cursorIndexOfResultado);
        _item.setResultado(_tmpResultado);
        final int _tmpSolucion;
        _tmpSolucion = _cursor.getInt(_cursorIndexOfSolucion);
        _item.setSolucion(_tmpSolucion);
        final int _tmpIdNivel;
        _tmpIdNivel = _cursor.getInt(_cursorIndexOfIdNivel);
        _item.setIdNivel(_tmpIdNivel);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Nivel getNivelPorID(int idnivel) {
    final String _sql = "SELECT * FROM nivel WHERE IdNivel = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idnivel);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfTitulo = _cursor.getColumnIndexOrThrow("titulo");
      final int _cursorIndexOfEspacios = _cursor.getColumnIndexOrThrow("espacios");
      final int _cursorIndexOfResultado = _cursor.getColumnIndexOrThrow("resultado");
      final int _cursorIndexOfSolucion = _cursor.getColumnIndexOrThrow("solucion");
      final int _cursorIndexOfIdNivel = _cursor.getColumnIndexOrThrow("idNivel");
      final Nivel _result;
      if(_cursor.moveToFirst()) {
        _result = new Nivel();
        final String _tmpTitulo;
        _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
        _result.setTitulo(_tmpTitulo);
        final int _tmpEspacios;
        _tmpEspacios = _cursor.getInt(_cursorIndexOfEspacios);
        _result.setEspacios(_tmpEspacios);
        final String _tmpResultado;
        _tmpResultado = _cursor.getString(_cursorIndexOfResultado);
        _result.setResultado(_tmpResultado);
        final int _tmpSolucion;
        _tmpSolucion = _cursor.getInt(_cursorIndexOfSolucion);
        _result.setSolucion(_tmpSolucion);
        final int _tmpIdNivel;
        _tmpIdNivel = _cursor.getInt(_cursorIndexOfIdNivel);
        _result.setIdNivel(_tmpIdNivel);
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
