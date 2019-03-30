package com.rockbass2560.megacode.views.activities;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuthException;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.helpers.ViewHelper;
import com.rockbass2560.megacode.models.Usuario;
import com.rockbass2560.megacode.viewmodels.LoginViewModel;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class RegisterActivity extends FragmentActivity {

    private final static String TAG = "RegisterActivity";
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;
    private LoginViewModel loginViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        ProgressBar progressBar = findViewById(R.id.progressbar_register);
        MaterialButton registrarButton = findViewById(R.id.button_registrarse);
        spinnerSex = findViewById(R.id.login_spinner_sexo);
        nameTextEdit = findViewById(R.id.login_text_name);
        ageTextEdit = findViewById(R.id.login_text_age);
        emailTextEdit = findViewById(R.id.login_text_email);
        contrasenaTextEdit = findViewById(R.id.login_text_contrasena);
        contrasena2TextEdit = findViewById(R.id.login_text_contrasena2);

        loginViewModel.observadorUsuario().observe(this, user ->{
            if (user != null){
                Intent intent = new Intent(this, RootActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setTitle("No se ha podido realizar el registro")
                .setPositiveButton("Ok", null);

        loginViewModel.observadorError().observe(this, error ->{
            progressBar.setVisibility(ProgressBar.GONE);
            registrarButton.setEnabled(true);

            dialogBuilder.setMessage(error);
            dialogBuilder.show();
        });

        registrarButton.setOnClickListener(view -> {
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
                progressBar.setVisibility(ProgressBar.VISIBLE);
                registrarButton.setEnabled(false);

                //Se guarda en base de datos remoto y se obtiene el token
                Usuario usuario = new Usuario();
                usuario.nombre = nameTextEdit.getText().toString();
                usuario.sexo = spinnerSex.getText().toString();
                usuario.edad = Integer.parseInt(ageTextEdit.getText().toString());

                ViewHelper.closeKeyboard(this, view.getWindowToken());

                loginViewModel.registrarUsuario(emailTextEdit.getText().toString(), contrasenaTextEdit.getText().toString(), usuario);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sexos, android.R.layout.simple_dropdown_item_1line);
        spinnerSex.setAdapter(adapter);

        setFocusChildListener(findViewById(R.id.login_layout_root));
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
