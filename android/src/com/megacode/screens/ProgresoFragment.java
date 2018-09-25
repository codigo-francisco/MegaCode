package com.megacode.screens;


import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgresoFragment extends Fragment {

    private final static int PAGE_LIMIT = 2;
    private TabAdapter tabAdapter = null;

    public ProgresoFragment() {
        // Required empty public constructor
    }

    class TabAdapter extends FragmentPagerAdapter{

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

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
        public Fragment getItem(int i) {
            Fragment fragment = null;

            switch (i){
                case 0:
                    fragment = new SkillTree();
                    break;
                case 1:
                    fragment = new ScoreFragment();
                    break;
            }


            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_LIMIT;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progreso, container, false);

        ViewPager viewPager = view.findViewById(R.id.progreso_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_progreso);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
