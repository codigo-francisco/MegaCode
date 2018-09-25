package com.megacode.screens;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megacode.services.RuleInstance;
import com.megacode.adapters.model.DataModel;
import com.megacode.models.FeedBack;
import com.megacode.models.Persona;
import com.megacode.models.response.NivelResponse;
import com.megacode.models.response.PosicionesResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.MegaCodeServiceInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private ArrayList<DataModel> data;
    private final static String TAG = "FeedFragment";
    private Persona persona;
    private CustomAdapter customAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        try {
            persona = Persona.buildPersonaFromJson(PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getString(getString(R.string.persona),null));

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feed);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            if (data==null)
                //Datos vacios para el feed
                data = new ArrayList<>();
            customAdapter = new CustomAdapter(data);

            //actualizar el feed...
            actualizarFeed();

            recyclerView.setAdapter(customAdapter);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return view;
    }

    public void actualizarFeed(){
        //Posicion contra otros
        MegaCodeService megaCodeService = MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService;
        megaCodeService.posicionContraOtros(persona.getToken(), persona.getId()).clone().enqueue(
                new Callback<List<PosicionesResponse>>() {
                    @Override
                    public void onResponse(Call<List<PosicionesResponse>> call,
                                           Response<List<PosicionesResponse>> response) {
                        DataModel dataModel=null;

                        if (response.isSuccessful()){
                            List<PosicionesResponse> listDataModel = response.body();
                            if (listDataModel.size()==1){
                                //Cuando es uno
                                PosicionesResponse posicionesResponse = listDataModel.get(0);
                                dataModel = new DataModel();
                                dataModel.setContent(
                                        String.format(Locale.getDefault(),
                                                "%s te ha superado con %d puntos en total, sigue compitiendo",
                                                posicionesResponse.getNombre(),
                                                posicionesResponse.getTotal()
                                        )
                                 );
                            }else if (listDataModel.size()>1){
                                dataModel = new DataModel();
                                String message = "%s y %s te han superado con %d y %d puntos en total";
                                if (listDataModel.size()>2)
                                    message = message.concat(" así como otros %d jugadores");
                                message = message.concat(", sigue compitiendo");
                                dataModel.setContent(
                                        String.format(Locale.getDefault(),
                                                message,
                                                listDataModel.get(0).getNombre(),
                                                listDataModel.get(1).getNombre(),
                                                listDataModel.get(0).getTotal(),
                                                listDataModel.get(1).getTotal(),
                                                listDataModel.size()-2
                                        )
                                );
                            }
                            if (dataModel!=null)
                                dataModel.setImagen(R.drawable.ic_baseline_bar_chart_24px);
                                dataModel.setTitle("Sigue compitiendo, no te quedes atras");
                                data.add(dataModel);
                                customAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PosicionesResponse>> call, Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                }
        );

        megaCodeService.siguienteEjercicio(persona.getToken(), persona.getId()).clone().enqueue(
                new Callback<NivelResponse>() {
                    @Override
                    public void onResponse(Call<NivelResponse> call, Response<NivelResponse> response) {
                        if (response.isSuccessful()){
                            NivelResponse nivelResponse = response.body();

                            DataModel dataModel = new DataModel();
                            dataModel.setTitle("Vamos a jugar");
                            dataModel.setContent(String.format(Locale.getDefault(), "Comienza a jugar, prueba el nivel %s", nivelResponse.getNombre()));
                            dataModel.setImagen(R.drawable.ic_baseline_videogame_asset_24px);

                            data.add(dataModel);
                            customAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<NivelResponse> call, Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                }
        );

        //Feedbacks
        List<FeedBack> feedBacks = RuleInstance.getRuleInstance(persona).getFeedbacks();
        for (FeedBack feedBack: feedBacks){
            DataModel dataModel = new DataModel();
            dataModel.setTitle(feedBack.getTitulo());
            dataModel.setContent(feedBack.getContenido());
            dataModel.setImagen(R.drawable.ic_baseline_info_24px);

            data.add(dataModel);
        }
        if (feedBacks.size()>0)
            customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
