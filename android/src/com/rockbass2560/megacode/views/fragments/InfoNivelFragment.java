package com.rockbass2560.megacode.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.helpers.MetricsHelper;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelConTerminado;
import com.rockbass2560.megacode.views.activities.MegaCodeAcitivity;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class InfoNivelFragment extends DialogFragment {

    private static final String TAG = DialogFragment.class.getName();
    private static final String LINESEPARATOR = System.lineSeparator();
    private Fragment fragment;

    public InfoNivelFragment(Fragment fragment){
        super();
        this.fragment = fragment;
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


            mensaje.append(String.format(Locale.getDefault(),"Ejercicio de %s%s", nivel.tipoNivelString(), LINESEPARATOR));

            //Si es mayor a 0 tiene información
            if (nivel.comandos > 0){
                mensaje.append(convertTemplateToString(nivel.comandos,"Comandos"));
            }

            if (nivel.si > 0){
               mensaje.append(convertTemplateToString(nivel.si, "Si"));
            }

            if (nivel.para > 0){
                mensaje.append(convertTemplateToString(nivel.para, "Para"));
            }

            if (nivel.mientras > 0){
                mensaje.append(convertTemplateToString(nivel.mientras, "Mientras"));
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

            String rutaNivel = nivel.ruta;
            MaterialButton button = view.findViewById(R.id.popup_boton_comenzar);
            button.setTextColor(backGroundColor);
            button.setOnClickListener(viewButton ->{
                Intent megaCodeIntent = new Intent(getActivity(), MegaCodeAcitivity.class);
                megaCodeIntent.putExtra("nivel", nivel);
                megaCodeIntent.putExtra(Claves.DIFICULTAD_DATA, getArguments().getSerializable(Claves.DIFICULTAD_DATA));
                fragment.startActivityForResult(megaCodeIntent, Claves.ABRIR_NIVEL_MEGACODE);
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
