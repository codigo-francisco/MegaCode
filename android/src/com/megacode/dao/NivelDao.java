package com.megacode.dao;

import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class NivelDao implements IDao<Nivel> {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Nivel element);

    @Insert
    public abstract void insertAll(List<Nivel> niveles);

    @Update
    public abstract void update(Nivel element);

    @Delete
    public abstract void delete(Nivel element);

    @Query("DELETE FROM Nivel")
    public abstract void borrarTodos();

    @Query("SELECT * FROM Nivel")
    public abstract List<Nivel> getNiveles();

    @Transaction
    @Query("SELECT * FROM Nivel")
    public abstract List<NivelConTerminado> getNivelesConTerminados();
}
