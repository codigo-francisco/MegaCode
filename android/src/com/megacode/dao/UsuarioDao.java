package com.megacode.dao;

import com.megacode.models.database.Usuario;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class UsuarioDao implements IDao<Usuario> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Usuario usuario);

    @Update
    public abstract  void update(Usuario usuario);

    @Query("SELECT * FROM Usuario LIMIT 1")
    public abstract LiveData<Usuario> obtenerUsuario();

    @Query("SELECT * FROM Usuario LIMIT 1")
    public abstract Usuario obtenerUsuarioSync();

    @Query("SELECT COUNT(*) FROM Usuario LIMIT 1")
    public abstract int cantidadUsuario();

    @Query("DELETE FROM Usuario")
    public abstract void borrarTodos();
}
