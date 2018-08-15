package com.udacity.gamedev.gigagal.android;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private Persona persona;
    private static int REQUEST_GET_SINGLE_FILE = 1;
    private static int RESULT_OK = -1;
    private static String TAG = "PerfilFragment";
    private AppCompatImageButton fotoPerfil;
    private SliderLayout sliderLayout;

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
        persona = bundle.getParcelable("persona");

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Se colocan los valores
        ((TextView)fragmentView.findViewById(R.id.name_view)).setText(persona.getName());
        ((TextView)fragmentView.findViewById(R.id.text_age)).setText(String.format(Locale.getDefault(),"%d %s",persona.getAge(), getResources().getString(R.string.anios)));
        ((TextView)fragmentView.findViewById(R.id.text_sex)).setText(persona.getSex());

        fotoPerfil = fragmentView.findViewById(R.id.foto_perfil);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), REQUEST_GET_SINGLE_FILE);
            }
        });

        sliderLayout = fragmentView.findViewById(R.id.slider);

        Map<String, Integer> images = new HashMap<>();
        images.put("MegaCode", R.drawable.megacode);
        images.put("She-MegaCode", R.drawable.she_megacode);

        for(String name: images.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView.description(name)
                    .image(images.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.stopAutoCycle();
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return fragmentView;
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }
}
