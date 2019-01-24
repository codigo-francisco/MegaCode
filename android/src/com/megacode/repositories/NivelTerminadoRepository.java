package com.megacode.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.megacode.dao.NivelTerminadoDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.NivelTerminado;

public class NivelTerminadoRepository {

    private NivelTerminadoDao nivelTerminadoDao;

    public NivelTerminadoRepository(Application application){
        nivelTerminadoDao = DataBaseMegaCode.getDataBaseMegaCode(application).nivelTerminadoDao();
    }

    public void insertarNivelTerminadoSync(NivelTerminado nivelTerminado){
        nivelTerminadoDao.insert(nivelTerminado);
    }

    public void insertarNivelTerminado(NivelTerminado nivelTerminado){
        AsyncTask.execute( () -> { nivelTerminadoDao.insert(nivelTerminado); } );
    }

}
