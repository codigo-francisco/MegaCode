package com.megacode.repositories;

import android.util.Log;

import com.megacode.adapters.model.SkillNode;
import com.megacode.models.response.NivelesResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.interfaces.NivelService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NivelRepository {

    private MutableLiveData<LinkedList<List<SkillNode>>> listMutableLiveData;
    private LinkedList<List<SkillNode>> nodes;
    private final static String TAG = NivelRepository.class.getName();

    public NivelRepository(){
        listMutableLiveData = new MutableLiveData<>();
        nodes = new LinkedList<>();
    }

    public MutableLiveData<LinkedList<List<SkillNode>>> listarNiveles(){
        MegaCodeService.getServicio(NivelService.class).listarNiveles().enqueue(new Callback<List<NivelesResponse>>() {
            @Override
            public void onResponse(Call<List<NivelesResponse>> call, Response<List<NivelesResponse>> response) {
                if (response.isSuccessful()){
                    int grupoActual = 0;
                    List<SkillNode> skillNodes=null;

                    if (response.body()!=null) {
                        nodes.clear();
                        for (NivelesResponse nivelResponse : response.body()) {
                            if (nivelResponse.getGrupo() != grupoActual) {
                                if (skillNodes!=null && skillNodes.size() > 0){
                                    nodes.add(skillNodes);
                                }
                                grupoActual = nivelResponse.getGrupo();
                                skillNodes = new ArrayList<>();
                            }
                            SkillNode skillNode = new SkillNode(nivelResponse);
                            skillNodes.add(skillNode);
                        }
                        if (skillNodes.size()>0)
                            nodes.add(skillNodes);

                        listMutableLiveData.postValue(nodes);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NivelesResponse>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });

        return listMutableLiveData;
    }

    public MutableLiveData<LinkedList<List<SkillNode>>> getListMutableLiveData() {
        return listMutableLiveData;
    }
}
