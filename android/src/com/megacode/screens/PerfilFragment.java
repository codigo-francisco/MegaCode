package com.megacode.screens;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.megacode.models.Persona;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private static int REQUEST_GET_SINGLE_FILE = 1;
    private static int RESULT_OK = -1;
    private static String TAG = "PerfilFragment";
    private AppCompatImageButton fotoPerfil, buttonMegaCode, buttonSheMegaCode;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode == REQUEST_GET_SINGLE_FILE){
                if (data!=null){
                    fotoPerfil.setImageURI(data.getData());
                    fotoPerfil.setBackgroundResource(R.color.translucent);
                }
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        assert bundle != null;
        Persona persona = bundle.getParcelable("persona");

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Se colocan los valores
        ((TextView)fragmentView.findViewById(R.id.name_view)).setText(persona.getNombre());
        ((TextView)fragmentView.findViewById(R.id.text_age)).setText(String.format(Locale.getDefault(),"%d %s",persona.getEdad(), getResources().getString(R.string.anios)));
        ((TextView)fragmentView.findViewById(R.id.text_sex)).setText(persona.getSexo());

        fotoPerfil = fragmentView.findViewById(R.id.foto_perfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), REQUEST_GET_SINGLE_FILE);
            }
        });

        buttonMegaCode = fragmentView.findViewById(R.id.button_megacode);
        buttonMegaCode.setOnClickListener(clickListener);
        buttonSheMegaCode = fragmentView.findViewById(R.id.button_shemegacode);
        buttonSheMegaCode.setOnClickListener(clickListener);

        return fragmentView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.button_megacode:
                    buttonSheMegaCode.setBackgroundResource(0);
                    buttonMegaCode.setBackgroundResource(R.drawable.borders);
                    break;
                case R.id.button_shemegacode:
                    buttonMegaCode.setBackgroundResource(0);
                    buttonSheMegaCode.setBackgroundResource(R.drawable.borders);
                    break;
            }
        }
    };
}
