package com.rockbass2560.megacode.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rockbass2560.megacode.models.Usuario;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> usuarioLiveData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PerfilViewModel(@NonNull Application application) {
        super(application);

        usuarioLiveData = new MutableLiveData<>();
    }

    public LiveData<Usuario> consultarUsuario(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            db.document("Usuarios/" + user.getUid()).addSnapshotListener(
                    (documentSnapshot, e) -> {
                        if (e == null) {
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            if (user.getUid() != null && usuario != null) {
                                usuario.id = user.getUid();
                                usuario.email = user.getEmail();
                                usuarioLiveData.setValue(usuario);
                            }
                        }
                    }
            );
        }

        return usuarioLiveData;
    }
}
