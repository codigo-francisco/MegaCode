package com.megacode.base;

import android.content.Intent;
import android.widget.Toast;

import com.megacode.models.IDialog;
import com.megacode.viewmodels.LoginViewModel;
import com.megacode.viewmodels.UsuarioViewModel;
import com.megacode.views.activities.ActivityBase;

import androidx.lifecycle.ViewModelProviders;

public abstract class LoginApp extends ActivityBase {

    private final static String TAG = "ActivityBase";

    protected IDialog errorDialog;
    protected IDialog datosIncorrectosDialog;

    protected UsuarioViewModel usuarioViewModel;

    public LoginApp(){
        super();
        errorDialog = createDialog();
    }

    public abstract IDialog createDialog();

    public void loginApp(String email, String contrasena, Intent intent){

        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.loginUsuario(email, contrasena).observe(this, usuario -> {
            if (usuario!=null){
                if (!usuario.hasError())
                    startActivity(intent);
                else{
                    if (usuario.getErrorCode()==403){
                        if (datosIncorrectosDialog==null){
                            Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }else{
                            datosIncorrectosDialog.show();
                        }
                    }
                }
            } else {
                errorGeneralMessage.show();
            }
        });
    }
}
