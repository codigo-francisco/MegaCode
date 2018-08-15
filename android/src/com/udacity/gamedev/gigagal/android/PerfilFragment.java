package com.udacity.gamedev.gigagal.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    Persona persona;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        persona = bundle.getParcelable("persona");

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Se colocan los valores
        ((TextView)fragmentView.findViewById(R.id.name_view)).setText(persona.getName());
        /*((TextView)fragmentView.findViewById(R.id.text_age)).setText(String.valueOf(persona.getAge()));
        ((TextView)fragmentView.findViewById(R.id.text_sex)).setText(persona.getSex());*/

        return fragmentView;
    }

}
