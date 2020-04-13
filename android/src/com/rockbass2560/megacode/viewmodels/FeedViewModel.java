package com.rockbass2560.megacode.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.adapters.model.DataModel;
import com.rockbass2560.megacode.adapters.model.enumators.TypeFeed;
import com.rockbass2560.megacode.models.FeedBack;
import com.rockbass2560.megacode.models.Usuario;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelTerminado;
import com.rockbass2560.megacode.services.RuleInstance;
import com.rockbass2560.megacode.views.activities.MegaCodeAcitivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FeedViewModel extends AndroidViewModel {

    private static final String TAG = FeedViewModel.class.getName();

    private List<DataModel> feeds;
    private MutableLiveData<List<DataModel>> feedsLiveData;
    private MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();

    public FeedViewModel(@NonNull Application application) {
        super(application);

        feeds = new ArrayList<>();
        feedsLiveData = new MutableLiveData<>();
    }

    public LiveData<Usuario> observadorUsuario(){
        return usuarioLiveData;
    }

    public void actualizarFeed(boolean borrarFeed){
        if (borrarFeed)
            feeds.clear();

        posicionContraOtros();
        siguienteEjercicio();
        feedbacks();
    }

    private void feedbacks(){
        try {
            String idUsuario = FirebaseAuth.getInstance().getUid();
            FirebaseFirestore.getInstance().document("Usuarios/"+idUsuario)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.getData()!=null) {
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            List<FeedBack> feedBacks = RuleInstance.getRuleInstance(usuario).getFeedbacks();
                            for (FeedBack feedBack : feedBacks) {
                                DataModel dataModel = new DataModel();
                                dataModel.setTypeFeed(TypeFeed.CONSEJO);
                                dataModel.setTitle(feedBack.getTitulo());
                                dataModel.setContent(feedBack.getContenido());
                                dataModel.setImagen(R.drawable.ic_baseline_info_24px);

                                feeds.add(dataModel);
                            }

                            feedsLiveData.setValue(feeds);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void siguienteEjercicio(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Niveles
        db.collection("Niveles").get()
                .addOnSuccessListener(nivelesQuery -> {
                    //Niveles terminados del usuario
                    String uid = FirebaseAuth.getInstance().getUid();
                    db.collection("Usuarios/"+uid+"/NivelesTerminados")
                            .get().addOnSuccessListener(nivelesTerminadosQuery ->{

                                List<NivelTerminado> nivelesTerminados = nivelesTerminadosQuery
                                        .getDocuments().stream().map(ntq -> {
                                            NivelTerminado nivelTerminado = ntq.toObject(NivelTerminado.class);
                                            nivelTerminado.id = ntq.getId();
                                            return nivelTerminado;
                                        }).collect(Collectors.toList());

                                List<Nivel> niveles = nivelesQuery.getDocuments()
                                        .stream().map(nq -> {
                                             Nivel nivel = nq.toObject(Nivel.class);
                                             nivel.id = Integer.parseInt(nq.getId());
                                             return nivel;
                                        }).collect(Collectors.toList());

                                niveles = niveles.stream().filter(n -> nivelesTerminados.stream().anyMatch(nt -> nt.nivelId == n.id && !nt.terminado)).collect(Collectors.toList());

                                if (niveles.size() > 0) {
                                    final Nivel nivel = niveles.stream().findFirst().get();
                                    feeds.add(createDataModelJuego(nivel.nombre, nivel.ruta));
                                    feedsLiveData.setValue(feeds);
                                }

                    });
                });
    }

    private DataModel createDataModelJuego(String nombreNivel, String rutaNivel){
        DataModel dataModel = new DataModel();
        dataModel.setTypeFeed(TypeFeed.JUEGO);
        dataModel.setTitle("Vamos a jugar");
        dataModel.setContent(String.format(Locale.getDefault(), "Comienza a jugar, prueba el nivel %s", nombreNivel));
        dataModel.setClickListener(view -> {
            Intent megaCodeIntent = new Intent(view.getContext(), MegaCodeAcitivity.class);
            megaCodeIntent.putExtra("rutaNivel", rutaNivel);
            view.getContext().startActivity(megaCodeIntent);
        });
        dataModel.setImagen(R.drawable.ic_baseline_videogame_asset_24px);

        return dataModel;
    }

    private void posicionContraOtros(){
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Usuarios")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        try {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                String id = FirebaseAuth.getInstance().getUid();

                                int puntaje = queryDocumentSnapshots.getDocuments().stream()
                                        .filter(d -> d.getId().equals(id))
                                        .findFirst().get().toObject(Usuario.class)
                                        .getPuntajeTotal();

                                List<Usuario> usuarios = queryDocumentSnapshots.toObjects(Usuario.class);
                                usuarios = usuarios.stream().filter(u ->
                                        u.getPuntajeTotal() > puntaje).limit(4)
                                        .collect(Collectors.toList());
                                if (!usuarios.isEmpty()) {
                                    StringBuilder mensaje = new StringBuilder("Has sido superado por:");

                                    for (Usuario usuarioItem : usuarios) {
                                        int puntajeUsuarioItem = usuarioItem.getPuntajeTotal();
                                        mensaje.append(String.format("\n\t%s con %s puntos", usuarioItem.email, puntajeUsuarioItem));
                                    }

                                    DataModel dataModel = new DataModel();
                                    dataModel.setTypeFeed(TypeFeed.PUNTAJE);
                                    dataModel.setImagen(R.drawable.ic_podium);
                                    dataModel.setTitle("Sigue compitiendo");
                                    dataModel.setContent(mensaje.toString());

                                    feeds.add(dataModel);
                                    feedsLiveData.setValue(feeds);
                                }
                            }
                        }catch(NullPointerException ex){
                            Log.d(TAG, ex.getMessage(), ex);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<DataModel>> getFeeds() {
        return feedsLiveData;
    }
}
