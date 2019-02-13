package com.megacode.repositories;

import android.content.Context;

import com.megacode.dao.ConexionDao;
import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.Conexion;

import java.util.Calendar;
import java.util.concurrent.Executors;

public class ConexionRepository {
    private ConexionDao conexionDao;
    private UsuarioDao usuarioDao;

    public ConexionRepository(Context context){
        DataBaseMegaCode db = DataBaseMegaCode.getDataBaseMegaCode(context);
        conexionDao = db.conexionDao();
        usuarioDao = db.usuarioDao();
    }

    /**
     * Inserta una nueva conexión, con la fecha y hora actual como entrada
     */
    public void nuevaConexion(){
        Executors.newSingleThreadExecutor().execute(()->{
            Conexion conexion = new Conexion();
            Calendar calendar = Calendar.getInstance();
            Long timeStamp = calendar.getTimeInMillis();
            Long id = usuarioDao.obtenerUsuarioSync().getId();
            conexion.id = Long.parseLong(timeStamp.toString() + id.toString());
            conexion.entrada = calendar.getTime();
            conexion.usuarioId = id;

            conexionDao.insertar(conexion);
        });
    }

    public void insertar(Conexion conexion){
        Executors.newSingleThreadExecutor().execute(()-> conexionDao.insertar(conexion) );
    }

    public void actualizar(Conexion conexion){
        Executors.newSingleThreadExecutor().execute(()-> conexionDao.actualizar(conexion) );
    }
}
