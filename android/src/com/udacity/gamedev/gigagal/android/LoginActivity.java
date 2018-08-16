package com.udacity.gamedev.gigagal.android;

import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton materialButton;
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinnerSex = findViewById(R.id.login_spinner_sexo);
        nameTextEdit =  findViewById(R.id.login_text_name);
        ageTextEdit = findViewById(R.id.login_text_age);
        emailTextEdit = findViewById(R.id.login_text_email);
        contrasenaTextEdit = findViewById(R.id.login_text_contrasena);
        contrasena2TextEdit = findViewById(R.id.login_text_contrasena2);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sexos,android.R.layout.simple_dropdown_item_1line);
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

                if (todoValido){
                    //Se realiza el registro aquí

                }
            }
        });

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

        if (areNotEquals)
            ((TextInputLayout)contrasenaTextEdit.getParent().getParent()).setError("Las contraseñas no coinciden");
        else
            ((TextInputLayout)contrasenaTextEdit.getParent().getParent()).setError("");

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
