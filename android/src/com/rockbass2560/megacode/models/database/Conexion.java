package com.megacode.models.database;

import com.megacode.models.database.converters.DateConverter;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@TypeConverters(DateConverter.class)
@Entity(tableName = "Conexion",
        foreignKeys = @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "usuarioId"),
        indices = { @Index("usuarioId") }
)
public class Conexion {
    @PrimaryKey
    public long id;
    @NonNull
    public long usuarioId;
    @NonNull
    public Date entrada = Calendar.getInstance().getTime();
    @Nullable
    public Date salida;
    @Nullable
    public Integer duracion;
}
