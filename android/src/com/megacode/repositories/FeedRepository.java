package com.megacode.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.megacode.R;
import com.megacode.adapters.model.DataModel;
import com.megacode.adapters.model.enumators.TypeFeed;
import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.FeedBack;
import com.megacode.models.database.Usuario;
import com.megacode.models.response.NivelResponse;
import com.megacode.models.response.PosicionesResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.RuleInstance;
import com.megacode.services.interfaces.FeedService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedRepository {

    private MutableLiveData<List<DataModel>> dataModelMutableLiveData;
    private List<DataModel> data;
    private UsuarioDao usuarioDao;
    private final static String TAG = FeedRepository.class.getName();

    public FeedRepository(Application application){
        usuarioDao = DataBaseMegaCode.getDataBaseMegaCode(application).usuarioDao();
        data = new ArrayList<>();
        dataModelMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<DataModel>> actualizarFeed(boolean borrarFeed){

        if (borrarFeed)
            data.clear();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Usuario usuario = usuarioDao.obtenerUsuarioSync();

                String token = usuario.getToken();
                long id = usuario.getId();

                FeedService feedService = MegaCodeService.getServicio(FeedService.class);

                feedService.posicionContraOtros(token, id)
                        .enqueue(new Callback<List<PosicionesResponse>>() {

                            @Override
                            public void onResponse(Call<List<PosicionesResponse>> call, Response<List<PosicionesResponse>> response) {
                                if (response.isSuccessful()) {

                                    DataModel dataModel = null;
                                    List<PosicionesResponse> listPosicionesResponse = response.body();

                                    if (listPosicionesResponse.size() == 1) {
                                        //Cuando es uno
                                        PosicionesResponse posicionesResponse = listPosicionesResponse.get(0);
                                        dataModel = new DataModel();
                                        dataModel.setContent(
                                                String.format(Locale.getDefault(),
                                                        "%s te ha superado con %d puntos en total, sigue compitiendo",
                                                        posicionesResponse.getNombre(),
                                                        posicionesResponse.getTotal()
                                                )
                                        );
                                    } else if (listPosicionesResponse.size() > 1) { //Cuando son más de uno
                                        dataModel = new DataModel();
                                        String message = "%s y %s te han superado con %d y %d puntos en total";
                                        if (listPosicionesResponse.size() > 2)
                                            message = message.concat(" así como otros %d jugadores");
                                        message = message.concat(", sigue compitiendo");
                                        dataModel.setContent(
                                                String.format(Locale.getDefault(),
                                                        message,
                                                        listPosicionesResponse.get(0).getNombre(),
                                                        listPosicionesResponse.get(1).getNombre(),
                                                        listPosicionesResponse.get(0).getTotal(),
                                                        listPosicionesResponse.get(1).getTotal(),
                                                        listPosicionesResponse.size() - 2
                                                )
                                        );
                                    }
                                    if (dataModel!=null) {
                                        dataModel.setTypeFeed(TypeFeed.PUNTAJE);
                                        dataModel.setImagen(R.drawable.ic_podium);
                                        dataModel.setTitle("Sigue compitiendo");

                                        data.add(dataModel);
                                        dataModelMutableLiveData.postValue(data);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<PosicionesResponse>> call, Throwable t) {
                                Log.e(TAG, t.getMessage(), t);
                            }
                        });

                feedService.siguienteEjercicio(token, id).enqueue(new Callback<NivelResponse>() {
                    @Override
                    public void onResponse(Call<NivelResponse> call, Response<NivelResponse> response) {
                        if (response.isSuccessful()){
                            NivelResponse nivelResponse = response.body();

                            DataModel dataModel = new DataModel();
                            dataModel.setTypeFeed(TypeFeed.JUEGO);
                            dataModel.setTitle("Vamos a jugar");
                            dataModel.setContent(String.format(Locale.getDefault(), "Comienza a jugar, prueba el nivel %s", nivelResponse.getNombre()));
                            dataModel.setImagen(R.drawable.ic_baseline_videogame_asset_24px);

                            data.add(dataModel);
                            dataModelMutableLiveData.postValue(data);
                        }
                    }

                    @Override
                    public void onFailure(Call<NivelResponse> call, Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                });

                List<FeedBack> feedBacks = RuleInstance.getRuleInstance(usuario).getFeedbacks();
                for (FeedBack feedBack: feedBacks){
                    DataModel dataModel = new DataModel();
                    dataModel.setTypeFeed(TypeFeed.CONSEJO);
                    dataModel.setTitle(feedBack.getTitulo());
                    dataModel.setContent(feedBack.getContenido());
                    dataModel.setImagen(R.drawable.ic_baseline_info_24px);

                    data.add(dataModel);
                }
                if (feedBacks.size()>0)
                    dataModelMutableLiveData.postValue(data);
            }
        });


        return dataModelMutableLiveData;
    }

    public MutableLiveData<List<DataModel>> getDataModelMutableLiveData() {
        return dataModelMutableLiveData;
    }
}
