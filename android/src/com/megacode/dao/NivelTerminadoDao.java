package com.megacode.dao;

import com.megacode.models.database.NivelTerminado;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class NivelTerminadoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(NivelTerminado nivelTerminado);

    @Insert
    public abstract void insertAll(List<NivelTerminado> nivelesTerminados);

    @Update
    public abstract void update(NivelTerminado nivelTerminado);

    @Query("DELETE FROM NivelTerminado")
    public abstract void borrarTodos();

    @Query("SELECT * FROM NivelTerminado")
    public abstract List<NivelTerminado> obtenerNivelesTerminadosSync();
}
