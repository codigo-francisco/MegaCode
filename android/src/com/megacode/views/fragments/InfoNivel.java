package com.megacode.views.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.megacode.R;
import com.megacode.helpers.MetricsHelper;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.views.activities.MegaCodeAcitivity;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class InfoNivel extends DialogFragment {

    private static final String LINESEPARATOR = System.lineSeparator();

    public InfoNivel(){
        super();
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translucent);
        window.setDimAmount(0);
        window.setWindowAnimations(R.style.DialogAnimationFade);
    }

    private static String convertTemplateToString(int points, String tema){
        String plural="";
        if (points>1)
            plural="s";
        return String.format(Locale.getDefault(), "%s: %d punto%s%s", tema, points, plural, LINESEPARATOR);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        boolean bloqueado = getArguments().getBoolean("bloqueado");
        NivelConTerminado nivelConTerminado = getArguments().getParcelable("nivel");
        int puntaje = getArguments().getInt("puntaje");
        Nivel nivel = nivelConTerminado.nivel;

        if (bloqueado){
            view = inflater.inflate(R.layout.popup_nivel_block, container);
        }else if (nivel !=null) {
            StringBuilder mensaje = new StringBuilder();

            view = inflater.inflate(R.layout.popup_nivel, container);


            mensaje.append(String.format(Locale.getDefault(),"Ejercicio de %s%s", nivel.getTypeLevel().toString(), LINESEPARATOR));

            //Si es mayor a 0 tiene información
            if (nivel.getComando()>0){
                mensaje.append(convertTemplateToString(nivel.getComando(),"Comandos"));
            }

            if (nivel.getSi() > 0){
               mensaje.append(convertTemplateToString(nivel.getSi(), "Si"));
            }

            if (nivel.getPara()>0){
                mensaje.append(convertTemplateToString(nivel.getComando(), "Para"));
            }

            if (nivel.getMientras()>0){
                mensaje.append(convertTemplateToString(nivel.getComando(), "Mientras"));
            }

            TextView mensajeInformacion = view.findViewById(R.id.popup_nivel_informacion);
            mensajeInformacion.setText(mensaje);

            int backGroundColor;
            if (puntaje==100)
                backGroundColor = ContextCompat.getColor(getContext(), R.color.md_yellow_700);
            else
                backGroundColor = ContextCompat.getColor(getContext(), nivel.getColorBackground());

            RelativeLayout relativeLayout = view.findViewById(R.id.popup_relative_root);
            ((GradientDrawable)relativeLayout.getBackground()).setColor(backGroundColor);

            String rutaNivel = nivel.getRuta();
            MaterialButton button = view.findViewById(R.id.popup_boton_comenzar);
            button.setTextColor(backGroundColor);
            button.setOnClickListener(viewButton ->{
                Intent megaCodeIntent = new Intent(getContext(), MegaCodeAcitivity.class);
                megaCodeIntent.putExtra("nivel", nivel);
                startActivity(megaCodeIntent);
                dismiss();
            });
        }

        //Cambiar posicion
        int sourceX = getArguments().getInt("sourceX");
        int sourceY = getArguments().getInt("sourceY");
        int heightView = getArguments().getInt("heightView");

        setDialogPosition(sourceX, sourceY, heightView);

        return view;
    }

    private void setDialogPosition(int sourceX, int sourceY, int heightView) {

        Window window = getDialog().getWindow();

        // set "origin" to top left corner
        window.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);

        WindowManager.LayoutParams params = window.getAttributes();

        //params.x = sourceX - dpToPx(110); // about half of confirm button size left of source view
        params.y = sourceY - MetricsHelper.dpToPx((-heightView/2)+10, getContext()); // below source view

        window.setAttributes(params);
    }
}
