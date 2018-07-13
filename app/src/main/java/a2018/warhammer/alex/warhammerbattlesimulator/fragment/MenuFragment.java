package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import a2018.warhammer.alex.warhammerbattlesimulator.R;
import a2018.warhammer.alex.warhammerbattlesimulator.services.MusiqueIntentService;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class MenuFragment extends Fragment implements View.OnClickListener {

    View view;
    Button combat, statistiques, event;
    TextView titre;
    FragmentManager fm;
    FragmentTransaction ft;

    public MenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initialize();
        return view;
    }

    public void initialize() {

        combat = view.findViewById(R.id.BTN_menu_combat);
        combat.setOnClickListener(this);
        statistiques = view.findViewById(R.id.BTN_menu_statistiques);
        statistiques.setOnClickListener(this);
        event = view.findViewById(R.id.BTN_menu_event);
        event.setOnClickListener(this);
        titre = view.findViewById(R.id.TV_menu_soustitre);

    }

    @Override
    public void onClick(View v) {
        MusiqueIntentService.pressed(view.getContext());

        switch (v.getId()) {
            case R.id.BTN_menu_combat:
                fm = getFragmentManager();
                ft = fm.beginTransaction();

                ft.replace(R.id.FL_accueil, new CombatChoixUnFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.BTN_menu_statistiques:
                fm = getFragmentManager();
                ft = fm.beginTransaction();

                ft.replace(R.id.FL_accueil, new StatisitqueChoixFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.BTN_menu_event:
                fm = getFragmentManager();
                ft = fm.beginTransaction();

                ft.replace(R.id.FL_accueil, new EventFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

    }
}
