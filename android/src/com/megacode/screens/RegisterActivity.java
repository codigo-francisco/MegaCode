package com.megacode.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.megacode.base.ActivityBase;
import com.megacode.models.Persona;
import com.megacode.models.RegistroResponse;
import com.megacode.services.MegaCodeServiceInstance;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends ActivityBase {

    private final static String TAG = "RegisterActivity";
    private MaterialButton materialButton;
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;
    Persona persona;
    Toast message;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        materialButton = findViewById(R.id.button_registrarse);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    persona = buildPersona();

                    MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.registrar(persona)
                            .clone()
                            .enqueue(new Callback<RegistroResponse>() {
                                @Override
                                public void onResponse(Call<RegistroResponse> call, retrofit2.Response<RegistroResponse> response) {
                                    if (response.isSuccessful()){
                                        persona.setId(response.body().getId());
                                        persona.setToken(response.body().getToken());

                                        Realm realm = Realm.getDefaultInstance();
                                        //Se guarda en base de datos local
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                //Se crea el objeto persona en Realm
                                                realm.insert(persona);

                                                //Se manda a llamar la actividad principal
                                                Intent intentActivity = new Intent(getApplication(), RootActivity.class);
                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                preferences.edit().putString(getString(R.string.persona), persona.toJson()).apply();
                                                startActivity(intentActivity);
                                            }
                                        });
                                    }else if (response.code()==403) {
                                        alertDialog.show();
                                    }else {
                                        message.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<RegistroResponse> call, Throwable t) {
                                    Log.e(TAG, t.getMessage(), t);
                                    message.show();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private Persona buildPersona(){
        Persona persona = new Persona();
        persona.setNombre(nameTextEdit.getText().toString());
        persona.setEdad(Integer.parseInt(ageTextEdit.getText().toString()));
        persona.setSexo(spinnerSex.getText().toString());
        persona.setEmail(emailTextEdit.getText().toString());
        persona.setContrasena(contrasenaTextEdit.getText().toString());

        return persona;
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
