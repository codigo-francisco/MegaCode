package com.rockbass2560.megacode.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.models.Usuario;
import com.rockbass2560.megacode.models.database.Conexion;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<FirebaseUser> usuarioMutableLiveData;
    private final MutableLiveData<String> errorLiveData;
    private static final String TAG = LoginViewModel.class.getName();
    private SharedPreferences sharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        usuarioMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        sharedPreferences = application.getSharedPreferences(Claves.SHARED_MEGACODE_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Método del modelo para realizar el lógin
     * @param email Email del usuario
     * @param contrasena Contraseña del usuario
     */
    public void loginUsuario(String email, String contrasena){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, contrasena)
            .addOnSuccessListener(authResult -> {
                FirebaseUser user = authResult.getUser();
                usuarioMutableLiveData.setValue(user);
            }).addOnFailureListener(failure -> {
                Log.e(TAG, failure.getMessage(), failure);
                colocarError(failure);
        });
    }

    public void registrarNuevaConexion(){
        Conexion conexion = new Conexion();
        conexion.entrada = Calendar.getInstance().getTime().getTime();
        conexion.usuarioId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("Conexiones")
                .add(conexion)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    sharedPreferences.edit().putString(Claves.CONEXION_ID, id).commit();
                });
    }

    private void colocarError(Exception failure){
        if (failure instanceof FirebaseAuthException) {
            FirebaseAuthException exception = (FirebaseAuthException) failure;
            String errorCode = exception.getErrorCode();

            if (errorCode.equals("ERROR_WRONG_PASSWORD") ||
                    errorCode.equals("ERROR_INVALID_EMAIL") ||
                    errorCode.equals("ERROR_USER_NOT_FOUND")) {
                errorLiveData.setValue("Usuario o contraseña incorrectos");
            } else if (errorCode.equals("ERROR_EMAIL_TAKEN")) {
                errorLiveData.setValue("El correo electronico ya está en uso");
            } else if (errorCode.equals("ERROR_NETWORK_ERROR")) {
                errorLiveData.setValue("Hay un error en la red, por favor verifique la conexión");
            } else {
                errorLiveData.setValue("Ha ocurrido un error en el proceso");
            }
        }else{
            errorLiveData.setValue("Ha ocurrido un error en el proceso");
        }
    }

    public void registrarUsuario(String correo, String contraseña, Usuario usuario){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contraseña)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    usuarioMutableLiveData.setValue(user);
                    //Agregamos más información sobre el usuario
                    FirebaseFirestore.getInstance().collection("Usuarios")
                            .document(user.getUid())
                            .set(usuario);
                    //Generamos una nueva conexión
                    registrarNuevaConexion();
                })
                .addOnFailureListener(
                    error -> {
                        Log.e(TAG, error.getMessage(), error);
                        colocarError(error);
                    }
                );
    }

    public LiveData<FirebaseUser> observadorUsuario(){
        return usuarioMutableLiveData;
    }

    public LiveData<String> observadorError(){
        return errorLiveData;
    }

    public void validarUsuario(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && !currentUser.getUid().isEmpty()){
            registrarNuevaConexion();
            usuarioMutableLiveData.setValue(currentUser);
        }else{
            usuarioMutableLiveData.setValue(null);
        }
    }
}
