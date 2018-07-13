package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class StatistiqueCombatFragment extends Fragment {


    public StatistiqueCombatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_statistique_combat, container, false);
    }

}
