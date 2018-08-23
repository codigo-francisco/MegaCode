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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.megacode.models.Persona;
import com.squareup.moshi.Moshi;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private MaterialButton materialButton;
    private MaterialBetterSpinner spinnerSex;
    private TextInputEditText nameTextEdit, ageTextEdit, emailTextEdit, contrasenaTextEdit, contrasena2TextEdit;
    Persona persona;
    Toast message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        message = Toast.makeText(getApplicationContext(), "Ocurrió un error al intentar agregar al usuario",Toast.LENGTH_LONG);

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
                    try {
                        persona = new Persona();
                        persona.setNombre(nameTextEdit.getText().toString());
                        persona.setEdad(Integer.parseInt(ageTextEdit.getText().toString()));
                        persona.setSexo(spinnerSex.getText().toString());
                        persona.setEmail(emailTextEdit.getText().toString());
                        persona.setContrasena(contrasenaTextEdit.getText().toString());

                        JsonRequest jsonRequest = new JsonObjectRequest(
                                JsonObjectRequest.Method.POST,
                                "http://192.168.1.64/megacode/api/login/registrar",
                                new JSONObject(new Moshi.Builder().build().adapter(Persona.class).toJson(persona)),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            persona.setId(response.getLong("id"));

                                            Realm realm = Realm.getDefaultInstance();
                                            //Se guarda en base de datos local
                                            realm.executeTransaction(transactionUsuario);

                                            Intent intentActivity = new Intent(getApplication(), RootActivity.class);
                                            intentActivity.putExtra("persona", persona);
                                            startActivity(intentActivity);
                                        } catch (JSONException e) {
                                            Log.e(TAG, e.getMessage());
                                            message.show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error!=null && error.networkResponse!=null){
                                            try {
                                                Log.e(TAG,
                                                        String.format("Error code:%s Error message:%s",
                                                                error.networkResponse.statusCode,
                                                                new String(error.networkResponse.data, "UTF-8")
                                                        ),
                                                        error
                                                );
                                            } catch (UnsupportedEncodingException e) {
                                                Log.e(TAG, e.getMessage());
                                            }
                                        }

                                        message.show();
                                    }
                                }
                        );

                        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(jsonRequest);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        message.show();
                    }
                }
            }
        });
    }

    Realm.Transaction transactionUsuario = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            //Se crea el objeto persona en Realm
            realm.copyToRealm(persona);
        }
    };

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
