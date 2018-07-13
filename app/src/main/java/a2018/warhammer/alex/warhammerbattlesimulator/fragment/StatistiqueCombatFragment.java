package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class StatistiqueCombatFragment extends Fragment {

    private View view;
    private Context context;
    private String unite1, unite2,vainqueur;


    public StatistiqueCombatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_combat, container, false);
        context = view.getContext();
        initialize();
        setText();
        return view;
    }

    public void setText() {

    }

    public void initialize(){
        Bundle data = this.getArguments();
        unite1 = data.getString("unite1");
        unite2 = data.getString("unite2");
        vainqueur = data.getString("vainqueur");
    }


}
