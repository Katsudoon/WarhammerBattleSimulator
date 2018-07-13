package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class CombatFragment extends Fragment {
    private View view;
    private Context context;
    private String choixFaction2, choixUnite2, choixFaction1, choixUnite1;
    private TextView versus,combat1,combat2;

    public CombatFragment() {

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

    public void initialize(){
        Bundle bundle1 = this.getArguments();
        Bundle bundle2 = this.getArguments();
        choixFaction1 = bundle1.getString("faction1");
        choixUnite1 = bundle1.getString("unite1");
        choixFaction2 = bundle2.getString("faction2");
        choixUnite2 = bundle2.getString("unite2");

        combat1 = view.findViewById(R.id.TV_combat_choix1);
        combat2 = view.findViewById(R.id.TV_combat_choix2);
        versus = view.findViewById(R.id.TV_combat_versus);
    }

    public void setText(){
        combat1.setText(choixFaction1 + " / " +choixUnite1);
        combat2.setText(choixFaction2 + " / " +choixUnite2);
    }
}
