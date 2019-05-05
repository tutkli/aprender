package com.example.clara.aprender.Base_datos;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.example.clara.aprender.DAO.NivelDAO;
import com.example.clara.aprender.DAO.NivelDAO_Impl;
import com.example.clara.aprender.DAO.SolucionDAO;
import com.example.clara.aprender.DAO.SolucionDAO_Impl;
import com.example.clara.aprender.DAO.UsuarioDAO;
import com.example.clara.aprender.DAO.UsuarioDAO_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class Base_datos_Aprender_Impl extends Base_datos_Aprender {
  private volatile NivelDAO _nivelDAO;

  private volatile SolucionDAO _solucionDAO;

  private volatile UsuarioDAO _usuarioDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `nivel` (`titulo` TEXT, `espacios` INTEGER NOT NULL, `resultado` TEXT, `solucion` INTEGER NOT NULL, `idNivel` INTEGER NOT NULL, PRIMARY KEY(`idNivel`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `solucion` (`Solucion` TEXT, `IdUsuario` INTEGER NOT NULL, `IdNivel` INTEGER NOT NULL, `IdSolucion` INTEGER NOT NULL, PRIMARY KEY(`IdSolucion`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `usuario` (`Nombre` TEXT, `IdUsuario` INTEGER NOT NULL, PRIMARY KEY(`IdUsuario`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"9db97372abe8eebb91c01da67f44b715\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `nivel`");
        _db.execSQL("DROP TABLE IF EXISTS `solucion`");
        _db.execSQL("DROP TABLE IF EXISTS `usuario`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsNivel = new HashMap<String, TableInfo.Column>(5);
        _columnsNivel.put("titulo", new TableInfo.Column("titulo", "TEXT", false, 0));
        _columnsNivel.put("espacios", new TableInfo.Column("espacios", "INTEGER", true, 0));
        _columnsNivel.put("resultado", new TableInfo.Column("resultado", "TEXT", false, 0));
        _columnsNivel.put("solucion", new TableInfo.Column("solucion", "INTEGER", true, 0));
        _columnsNivel.put("idNivel", new TableInfo.Column("idNivel", "INTEGER", true, 1));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNivel = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNivel = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNivel = new TableInfo("nivel", _columnsNivel, _foreignKeysNivel, _indicesNivel);
        final TableInfo _existingNivel = TableInfo.read(_db, "nivel");
        if (! _infoNivel.equals(_existingNivel)) {
          throw new IllegalStateException("Migration didn't properly handle nivel(com.example.clara.aprender.Modelos.Nivel).\n"
                  + " Expected:\n" + _infoNivel + "\n"
                  + " Found:\n" + _existingNivel);
        }
        final HashMap<String, TableInfo.Column> _columnsSolucion = new HashMap<String, TableInfo.Column>(4);
        _columnsSolucion.put("Solucion", new TableInfo.Column("Solucion", "TEXT", false, 0));
        _columnsSolucion.put("IdUsuario", new TableInfo.Column("IdUsuario", "INTEGER", true, 0));
        _columnsSolucion.put("IdNivel", new TableInfo.Column("IdNivel", "INTEGER", true, 0));
        _columnsSolucion.put("IdSolucion", new TableInfo.Column("IdSolucion", "INTEGER", true, 1));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSolucion = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSolucion = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSolucion = new TableInfo("solucion", _columnsSolucion, _foreignKeysSolucion, _indicesSolucion);
        final TableInfo _existingSolucion = TableInfo.read(_db, "solucion");
        if (! _infoSolucion.equals(_existingSolucion)) {
          throw new IllegalStateException("Migration didn't properly handle solucion(com.example.clara.aprender.Modelos.Solucion).\n"
                  + " Expected:\n" + _infoSolucion + "\n"
                  + " Found:\n" + _existingSolucion);
        }
        final HashMap<String, TableInfo.Column> _columnsUsuario = new HashMap<String, TableInfo.Column>(2);
        _columnsUsuario.put("Nombre", new TableInfo.Column("Nombre", "TEXT", false, 0));
        _columnsUsuario.put("IdUsuario", new TableInfo.Column("IdUsuario", "INTEGER", true, 1));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsuario = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsuario = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuario = new TableInfo("usuario", _columnsUsuario, _foreignKeysUsuario, _indicesUsuario);
        final TableInfo _existingUsuario = TableInfo.read(_db, "usuario");
        if (! _infoUsuario.equals(_existingUsuario)) {
          throw new IllegalStateException("Migration didn't properly handle usuario(com.example.clara.aprender.Modelos.Usuario).\n"
                  + " Expected:\n" + _infoUsuario + "\n"
                  + " Found:\n" + _existingUsuario);
        }
      }
    }, "9db97372abe8eebb91c01da67f44b715", "e841867526aa1598262dc842af72a225");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "nivel","solucion","usuario");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `nivel`");
      _db.execSQL("DELETE FROM `solucion`");
      _db.execSQL("DELETE FROM `usuario`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public NivelDAO getNivelDAO() {
    if (_nivelDAO != null) {
      return _nivelDAO;
    } else {
      synchronized(this) {
        if(_nivelDAO == null) {
          _nivelDAO = new NivelDAO_Impl(this);
        }
        return _nivelDAO;
      }
    }
  }

  @Override
  public SolucionDAO getSolucionDAO() {
    if (_solucionDAO != null) {
      return _solucionDAO;
    } else {
      synchronized(this) {
        if(_solucionDAO == null) {
          _solucionDAO = new SolucionDAO_Impl(this);
        }
        return _solucionDAO;
      }
    }
  }

  @Override
  public UsuarioDAO getUsuarioDAO() {
    if (_usuarioDAO != null) {
      return _usuarioDAO;
    } else {
      synchronized(this) {
        if(_usuarioDAO == null) {
          _usuarioDAO = new UsuarioDAO_Impl(this);
        }
        return _usuarioDAO;
      }
    }
  }
}
