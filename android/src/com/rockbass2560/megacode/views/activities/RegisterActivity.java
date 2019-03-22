package com.rockbass2560.megacode.views.activities;

import android.content.Intent;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.helpers.ImageProfileHelper;
import com.rockbass2560.megacode.models.database.Usuario;
import com.rockbass2560.megacode.viewmodels.LoginViewModel;
import com.rockbass2560.megacode.viewmodels.UsuarioViewModel;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import androidx.lifecycle.ViewModelProviders;

public class RegisterActivity extends ActivityBase {

    private final static String TAG = "RegisterActivity";
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;
    private Usuario usuario;
    private Toast message;
    private AlertDialog alertDialog;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getUsuarioMutableLiveData().observe(this, usuario -> {
            if (usuario!=null){
                if (!usuario.hasError()){
                    Intent intentActivity = new Intent(RegisterActivity.this, RootActivity.class);
                    intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentActivity);
                }else{
                    if (usuario.getErrorCode() == 403){
                        alertDialog.show();
                    }else{
                        message.show();
                    }
                }
            }else{
                message.show();
            }
        });

        MaterialButton materialButton = findViewById(R.id.button_registrarse);
        materialButton.setOnClickListener(view -> {
            //Validar campos
            boolean name, age, sex, email, contrasena, contrasena2, contrasenaDiferentes = false;

            name = validationEmptyEditText(nameTextEdit, "Debe introducir un nombre");
            age = validationEmptyEditText(ageTextEdit, "Introduzca una edad");
            sex = validationEmptyEditText(spinnerSex, "Seleccione una opción");
            email = validationEmptyEditText(emailTextEdit, "Introduzca un email") && validarEmail();
            contrasena = validationEmptyEditText(contrasenaTextEdit, "Introduzca una contraseña");
            contrasena2 = validationEmptyEditText(contrasena2TextEdit, "Confirme su contraseña");
            contrasenaDiferentes = (contrasena && contrasena2) && validarContrasenasIguales();

            boolean todoValido = name && age && sex && email && contrasena && contrasena2 && contrasenaDiferentes;

            if (todoValido) {
                //Se guarda en base de datos remoto y se obtiene el token
                usuario = buildPersona();

                loginViewModel.registrarUsuario(usuario);
            }
        });

        message = Toast.makeText(this, "Ocurrió un error al intentar agregar al usuario",Toast.LENGTH_LONG);
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("El email se encuentra registrado")
                .setPositiveButton("Ok", null)
                .create();

        spinnerSex = findViewById(R.id.login_spinner_sexo);
        nameTextEdit = findViewById(R.id.login_text_name);
        ageTextEdit = findViewById(R.id.login_text_age);
        emailTextEdit = findViewById(R.id.login_text_email);
        contrasenaTextEdit = findViewById(R.id.login_text_contrasena);
        contrasena2TextEdit = findViewById(R.id.login_text_contrasena2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexos, android.R.layout.simple_dropdown_item_1line);
        spinnerSex.setAdapter(adapter);

        setFocusChildListener(findViewById(R.id.login_layout_root));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private Usuario buildPersona(){
        Usuario usuario = new Usuario();
        usuario.setNombre(nameTextEdit.getText().toString());
        usuario.setEdad(Integer.parseInt(ageTextEdit.getText().toString()));
        usuario.setSexo(spinnerSex.getText().toString());
        usuario.setEmail(emailTextEdit.getText().toString());
        usuario.setContrasena(contrasenaTextEdit.getText().toString());
        //Establecer imagen de perfil por default
        usuario.setFotoPerfil(ImageProfileHelper.getDefaultProfileImage(getAssets()));

        return usuario;
    }

    private void setFocusChildListener(View view){
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int index = 0; index < viewGroup.getChildCount(); index++) {
                View childView = viewGroup.getChildAt(index);
                setFocusChildListener(childView);
            }
        }else if (view instanceof TextInputEditText || view instanceof MaterialBetterSpinner) {
            view.setOnFocusChangeListener(focusChangeListener);
        }
    }

    private boolean validarContrasenasIguales(){
        boolean areNotEquals = !TextUtils.equals(contrasenaTextEdit.getText(), contrasena2TextEdit.getText());

        if (areNotEquals) {
            ((TextInputLayout) contrasenaTextEdit.getParent().getParent()).setError("Las contraseñas no coinciden");
            ((TextInputLayout) contrasena2TextEdit.getParent().getParent()).setError("Las contraseñas no coinciden");
        }
        else {
            ((TextInputLayout) contrasenaTextEdit.getParent().getParent()).setError("");
            ((TextInputLayout) contrasena2TextEdit.getParent().getParent()).setError("");
        }

        return !areNotEquals;
    }

    private boolean validarEmail(){
        CharSequence emailText = emailTextEdit.getText();

        boolean isNotValid = !Patterns.EMAIL_ADDRESS.matcher(emailText).matches();

        if(isNotValid)
            ((TextInputLayout)emailTextEdit.getParent().getParent()).setError("Introduzca un email valido");
        else
            ((TextInputLayout)emailTextEdit.getParent().getParent()).setError("");

        return !isNotValid;
    }

    private boolean validationEmptyEditText(EditText editText, String errorMessage){
        CharSequence text =  editText.getText();

        boolean isEmpty = TextUtils.isEmpty(text);
        if (isEmpty)
            ((TextInputLayout)editText.getParent().getParent()).setError(errorMessage);
        else
            ((TextInputLayout)editText.getParent().getParent()).setError("");

        return !isEmpty;
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus){
                int id = view.getId();
                switch(id){
                    case R.id.login_text_name:
                        validationEmptyEditText(nameTextEdit, "Debe introducir un nombre");
                        break;
                    case R.id.login_text_age:
                        validationEmptyEditText(ageTextEdit, "Introduzca una edad");
                        break;
                    case R.id.login_spinner_sexo:
                        validationEmptyEditText(spinnerSex, "Seleccione una opción");
                        break;
                    case R.id.login_text_email:
                        if (validationEmptyEditText(emailTextEdit, "Introduzca un email"))
                            validarEmail();
                        break;
                    case R.id.login_text_contrasena:
                        validationEmptyEditText(contrasenaTextEdit, "Introduzca una contraseña");
                        break;
                    case R.id.login_text_contrasena2:
                        validationEmptyEditText(contrasena2TextEdit, "Confirme su contraseña");
                        break;
                }
            }
        }
    };
}
