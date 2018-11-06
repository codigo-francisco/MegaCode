package com.megacode.views.fragments;

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
import com.megacode.models.database.Nivel;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class InfoNivel extends DialogFragment {

    public InfoNivel(){
        super();
    }

    private Nivel node;

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translucent);
        window.setDimAmount(0);
        window.setWindowAnimations(R.style.DialogAnimationFade);
    }

    private String convertTemplateToString(int points, String tema){
        String plural="";
        if (points>1)
            plural="s";
        return String.format(Locale.getDefault(), "%d punto%s", points, plural, tema);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_nivel, container); //return super.onCreateView(inflater, container, savedInstanceState);

        node = getArguments().getParcelable("node");


        if (node!=null) {
            TextView textTitulo = view.findViewById(R.id.popup_titulo);
            textTitulo.setText(String.format(Locale.getDefault(),"Ejercicio de %s",node.getTypeLevel().toString()));

            //Si es mayor a 0 tiene información
            if (node.getComando()>0){
                TextView textComando = view.findViewById(R.id.popup_comando);
                textComando.setText(convertTemplateToString(node.getComando(),"comandos"));

                textComando.setVisibility(View.VISIBLE);
                view.findViewById(R.id.popup_text_comando).setVisibility(View.VISIBLE);
            }

            if (node.getSi() > 0){
                TextView textSi = view.findViewById(R.id.popup_si);
                textSi.setText(convertTemplateToString(node.getSi(), "si"));

                textSi.setVisibility(View.VISIBLE);
                view.findViewById(R.id.popup_text_si).setVisibility(View.VISIBLE);
            }

            if (node.getPara()>0){
                TextView textPara = view.findViewById(R.id.popup_para);
                textPara.setText(convertTemplateToString(node.getComando(), "para"));

                textPara.setVisibility(View.VISIBLE);
                view.findViewById(R.id.popup_text_para).setVisibility(View.VISIBLE);
            }

            if (node.getMientras()>0){
                TextView textMientras = view.findViewById(R.id.popup_mientra);
                textMientras.setText(convertTemplateToString(node.getComando(), "mientras"));

                textMientras.setVisibility(View.VISIBLE);
                view.findViewById(R.id.popup_text_mientras).setVisibility(View.VISIBLE);
            }

            int backGroundColor = ContextCompat.getColor(getContext(), getColorBackground());
            RelativeLayout relativeLayout = view.findViewById(R.id.popup_relative_root);
            ((GradientDrawable)relativeLayout.getBackground()).setColor(backGroundColor);
            MaterialButton button = view.findViewById(R.id.popup_boton_comenzar);
            button.setTextColor(backGroundColor);

            //Cambiar posicion
            int sourceX = getArguments().getInt("sourceX");
            int sourceY = getArguments().getInt("sourceY");
            int heightView = getArguments().getInt("heightView");

            setDialogPosition(sourceX, sourceY, heightView);
        }

        return view;
    }

    private int getColorBackground(){
        int color=0;

        switch (node.getTypeLevel()){
            case COMANDO:
                color = R.color.md_brown_600;
                break;
            case SI:
                color = R.color.md_blue_700;
                break;
            case PARA:
                color = R.color.md_yellow_700;
                break;
            case MIENTRAS:
                color = R.color.md_purple_600;
                break;
            default:
                color = R.color.md_blue_700;
                break;
        }

        return color;
    }

    private void setDialogPosition(int sourceX, int sourceY, int heightView) {

        Window window = getDialog().getWindow();

        // set "origin" to top left corner
        window.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);

        WindowManager.LayoutParams params = window.getAttributes();

        //params.x = sourceX - dpToPx(110); // about half of confirm button size left of source view
        params.y = sourceY - dpToPx((-heightView/2)+10); // below source view

        window.setAttributes(params);
    }

    public int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
