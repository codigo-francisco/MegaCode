package com.megacode.views.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import com.megacode.Claves;
import com.megacode.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgresoFragment extends Fragment {

    private final static int PAGE_LIMIT = 2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Claves.ABRIR_NIVEL_MEGACODE){
            if (resultCode == Activity.RESULT_OK)
                tabAdapter.getFragment(0).onActivityResult(requestCode, resultCode, data);
        }
    }

    public ProgresoFragment() {
        // Required empty public constructor
    }

    class TabAdapter extends FragmentPagerAdapter{

        TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        private SparseArray<Fragment> fragments = new SparseArray<>();

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = null;

            switch (position){
                case 0:
                    title = "Niveles";
                    break;
                case 1:
                    title = "Puntaciones";
                    break;
            }

            return title;
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment = null;
            switch (index){
                case 0:
                    fragment = new SkillTreeFragment();
                    break;
                case 1:
                    fragment = new ScoreFragment();
                    break;
            }

            fragments.put(index, fragment);

            return fragment;
        }

        public Fragment getFragment(int index){
            return fragments.get(index);
        }

        @Override
        public int getCount() {
            return PAGE_LIMIT;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);

            fragments.remove(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progreso, container, false);

        ViewPager viewPager = view.findViewById(R.id.progreso_pager);
        tabLayout = view.findViewById(R.id.tab_progreso);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
