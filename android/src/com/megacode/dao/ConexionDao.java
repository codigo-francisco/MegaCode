package com.megacode.dao;

import com.megacode.models.database.Conexion;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public abstract class ConexionDao {
    @Insert
    public abstract void insertar(Conexion conexion);

    @Update
    public abstract void actualizar(Conexion conexion);
}
