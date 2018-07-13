package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import a2018.warhammer.alex.warhammerbattlesimulator.R;
import a2018.warhammer.alex.warhammerbattlesimulator.services.MusiqueIntentService;


public class AccueilFragment extends Fragment implements View.OnClickListener {

    private Button connexion, inscription;
    private View view;

    public AccueilFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accueil, container, false);
        initialize();
        return view;
    }


    @Override
    public void onClick(View v) {
        MusiqueIntentService.pressed(view.getContext());

        switch (v.getId()) {
            case R.id.BTN_accueil_connexion:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FL_accueil, new ConnexionFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.BTN_accueil_inscription:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FL_accueil, new InscriptionFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }

    }

    public void initialize() {

        connexion = view.findViewById(R.id.BTN_accueil_connexion);
        connexion.setOnClickListener(this);
        inscription = view.findViewById(R.id.BTN_accueil_inscription);
        inscription.setOnClickListener(this);

    }
}
