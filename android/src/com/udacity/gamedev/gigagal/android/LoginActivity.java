package com.udacity.gamedev.gigagal.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.megacode.models.Persona;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private MaterialButton materialButton;
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

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
                    //Se realiza el registro aquí, queda entrada al sistema principal
                    auth.createUserWithEmailAndPassword(emailTextEdit.getText().toString(), contrasenaTextEdit.getText().toString())
                            .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //Se construye al usuario y se guarda en base de datos y en la memoria
                                        FirebaseUser user = authResult.getUser();
                                        Persona persona = new Persona(user.getUid(),Integer.parseInt(ageTextEdit.getText().toString())
                                                ,nameTextEdit.getText().toString()
                                                , spinnerSex.getText().toString());
                                        persona.setEmail(emailTextEdit.getText().toString());
                                        persona.setContrasena(contrasenaTextEdit.getText().toString());

                                        persona.setToken(user.getIdToken(false).getResult().getToken());

                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                        db.child("persona").child(persona.getId()).setValue(persona);

                                        Intent intentActivity = new Intent(getApplication(), RootActivity.class);
                                        intentActivity.putExtra("persona", persona);
                                        startActivity(intentActivity);
                                    }
                                }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(getApplicationContext(), "No se ha registrado el usuario, intentelo de nuevo", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //TODO: Quitar, solo para pruebas
                /*Intent intentActivity = new Intent(getApplication(), RootActivity.class);
                startActivity(intentActivity);*/
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
