package com.megacode.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.megacode.dao.NivelDao;
import com.megacode.dao.NivelTerminadoDao;
import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.models.database.NivelTerminado;
import com.megacode.models.database.Usuario;
import com.megacode.models.response.ListarNivelesResponse;
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

    private MutableLiveData<LinkedList<List<NivelConTerminado>>> nivelesLiveData;
    private NivelDao nivelDao;
    private UsuarioDao usuarioDao;
    private NivelTerminadoDao nivelTerminadoDao;
    private final static String TAG = NivelRepository.class.getName();

    public NivelRepository(Application application){
        DataBaseMegaCode dataBaseMegaCode = DataBaseMegaCode.getDataBaseMegaCode(application);
        nivelDao = dataBaseMegaCode.nivelDao();
        usuarioDao = dataBaseMegaCode.usuarioDao();
        nivelTerminadoDao = dataBaseMegaCode.nivelTerminadoDao();
        nivelesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LinkedList<List<NivelConTerminado>>> listarNiveles(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List <NivelConTerminado> niveles = nivelDao.getNivelesConTerminados();
                boolean needRefresh = true;

                if (niveles!=null && niveles.size()>0){
                    //Si no está fuera del rango de tiempo, entonces no necesita refrescar la información
                    needRefresh = isOverMaxTime(niveles.get(0).nivel.getLastRefresh());
                }

                if (!needRefresh){ //Los datos traidos de BD son organizados por niveles

                    nivelesLiveData.postValue(NivelConTerminado.organizarPorNiveles(niveles));

                }else{ //Traer del servidor
                    Usuario usuario = usuarioDao.obtenerUsuarioSync();
                    MegaCodeService.getServicio(NivelService.class).listarNiveles(usuario.getToken(), usuario.getId())
                            .enqueue(new Callback<ListarNivelesResponse>() {
                        @Override
                        public void onResponse(Call<ListarNivelesResponse> call, Response<ListarNivelesResponse> response) {
                            Log.d(TAG, "Respuesta de los datos de nivel recibidos");
                            if (response.isSuccessful()){

                                List<Nivel> niveles = response.body().getNiveles();
                                List<NivelTerminado> nivelesTerminados = response.body().getNivelesTerminados();

                                //Colocar el tipo de nivel... estoy considerando no utilizar el enumerador
                                for (Nivel nivel : niveles) {
                                    nivel.chooseTypeLevel();
                                }

                                //Se insertan ambos datos, una vez insertados se realiza la consulta
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Se borran los niveles viejos y se agregan los nuevos
                                        nivelTerminadoDao.borrarTodos();
                                        nivelDao.borrarTodos();

                                        nivelDao.insertAll(niveles);
                                        nivelTerminadoDao.insertAll(nivelesTerminados);

                                        List<NivelConTerminado> nivelesConTerminados = nivelDao.getNivelesConTerminados();

                                        //Se crea el arbol y se pasa a la vista
                                        LinkedList<List<NivelConTerminado>> nodes = NivelConTerminado.organizarPorNiveles(nivelesConTerminados);

                                        //Notifica los niveles terminados
                                        nivelesLiveData.postValue(nodes);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ListarNivelesResponse> call, Throwable t) {
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

    public MutableLiveData<LinkedList<List<NivelConTerminado>>> getNiveles() {
        return nivelesLiveData;
    }

    public void actualizarNivel(Nivel nivel){
        AsyncTask.execute(()-> nivelDao.update(nivel));
    }
}
