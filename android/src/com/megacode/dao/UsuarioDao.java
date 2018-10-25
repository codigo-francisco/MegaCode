package com.megacode.dao;

import com.megacode.models.database.Usuario;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class UsuarioDao implements IDao<Usuario> {

    @Insert
    public abstract void insert(Usuario usuario);

    @Update
    public abstract  void update(Usuario usuario);

    @Query("SELECT * FROM Usuario LIMIT 1")
    public abstract LiveData<Usuario> obtenerUsuario();

    @Query("DELETE FROM Usuario")
    public abstract void borrarTodos();
}
