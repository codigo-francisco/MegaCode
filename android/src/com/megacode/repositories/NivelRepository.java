package com.megacode.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.megacode.dao.NivelDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.Nivel;
import com.megacode.services.MegaCodeService;
import com.megacode.services.interfaces.NivelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NivelRepository {

    private MutableLiveData<LinkedList<List<Nivel>>> nivelesLiveData;
    private NivelDao nivelDao;
    private final static String TAG = NivelRepository.class.getName();

    public NivelRepository(Application application){
        DataBaseMegaCode dataBaseMegaCode = DataBaseMegaCode.getDataBaseMegaCode(application);
        nivelDao = dataBaseMegaCode.nivelDao();
        nivelesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<List<Nivel>>> listarNiveles(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List < Nivel > niveles = nivelDao.getNiveles();
                boolean needRefresh = true;

                if (niveles!=null && niveles.size()>0){
                    //Si no está fuera del rango de tiempo, entonces no necesita refrescar la información
                    needRefresh = isOverMaxTime(niveles.get(0).getLastRefresh());
                }
                if (!needRefresh){ //Los datos traidos de BD son organizados por niveles
                    nivelesLiveData.postValue(Nivel.organizarPorNiveles(niveles));
                }else{ //Traer del servidor
                    MegaCodeService.getServicio(NivelService.class).listarNiveles().enqueue(new Callback<List<Nivel>>() {
                        @Override
                        public void onResponse(Call<List<Nivel>> call, Response<List<Nivel>> response) {
                            if (response.isSuccessful()){
                                List<Nivel> niveles = response.body();

                                for (Nivel nivel : niveles) {
                                    nivel.chooseTypeLevelAndResource();
                                }

                                LinkedList<List<Nivel>> nodes = Nivel.organizarPorNiveles(niveles);

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        nivelDao.borrarTodos();
                                        nivelDao.insertAll(niveles);
                                    }
                                });

                                nivelesLiveData.postValue(nodes);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Nivel>> call, Throwable t) {
                            Log.e(TAG, t.getMessage(), t);
                        }
                    });
                }
            }
        });

        return nivelesLiveData;
    }

    private static boolean isOverMaxTime(Date lastRefresh){
        Calendar now = Calendar.getInstance();

        Calendar maxTime = Calendar.getInstance();
        maxTime.setTime(lastRefresh);
        maxTime.add(Calendar.HOUR, 24);

        return now.after(maxTime);
    }

    public MutableLiveData<LinkedList<List<Nivel>>> getNiveles() {
        return nivelesLiveData;
    }
}
