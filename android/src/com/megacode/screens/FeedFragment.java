package com.megacode.screens;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.Person;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megacode.models.TypeFeed;
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
import java.util.Objects;

import io.realm.Realm;
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
    private Realm realm;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        realm = Realm.getDefaultInstance();

        persona = realm.where(Persona.class).findFirst();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = view.findViewById(R.id.feed_refreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this::actualizarFeed);

        if (data==null)
            //Datos vacios para el feed
            data = new ArrayList<>();

        customAdapter = new CustomAdapter(data, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScoreActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelableArrayList("feeds")!=null){
                data.addAll(new ArrayList<>(Objects.requireNonNull(savedInstanceState.getParcelableArrayList("feeds"))));
                customAdapter.notifyDataSetChanged();
            }
        }else {
            //actualizar el feed...
            actualizarFeed();
        }


        recyclerView.setAdapter(customAdapter);

        return view;
    }

    public void actualizarFeed(){
        data.clear();
        //Posicion contra otros
        MegaCodeService megaCodeService = MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService;
        megaCodeService.posicionContraOtros(persona.getToken(), persona.getId()).clone().enqueue(
                new Callback<List<PosicionesResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<PosicionesResponse>> call,
                                           @NonNull Response<List<PosicionesResponse>> response) {
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
                                dataModel.setTypeFeed(TypeFeed.PUNTAJE);
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
                            dataModel.setTypeFeed(TypeFeed.JUEGO);
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
            dataModel.setTypeFeed(TypeFeed.CONSEJO);
            dataModel.setTitle(feedBack.getTitulo());
            dataModel.setContent(feedBack.getContenido());
            dataModel.setImagen(R.drawable.ic_baseline_info_24px);

            data.add(dataModel);
        }
        if (feedBacks.size()>0)
            customAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("feeds", new ArrayList<>(data));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (realm != null && !realm.isClosed())
            realm.close();
        super.onDestroy();
    }
}
