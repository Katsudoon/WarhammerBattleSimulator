package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.content.Context;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import a2018.warhammer.alex.warhammerbattlesimulator.MainActivity;
import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class CombatChoixDeuxFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private Spinner spinnerFaction, spinnerUnite;
    private Button valider;
    private String faction, unite, choixFaction2, choixUnite2, choixFaction1, choixUnite1;
    private DatabaseReference mDatabase, dbfaction, dbUnite;
    private List<String> factions, unites;

    public CombatChoixDeuxFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_combat_choix_deux, container, false);

        context = view.getContext();
        initialize();
        spinnerInitialize();
        Bundle data = this.getArguments();
        choixFaction1 = data.getString("faction1");
        choixUnite1 = data.getString("unite1");
        return view;
    }

    public void initialize() {
        spinnerFaction = view.findViewById(R.id.SP_choix2_faction2);
        spinnerFaction.setOnItemSelectedListener(this);
        spinnerUnite = view.findViewById(R.id.SP_choix2_unite2);
        spinnerUnite.setOnItemSelectedListener(this);
        valider = view.findViewById(R.id.BTN_choix2_valider);
        valider.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        choixFaction2 = (String) spinnerFaction.getSelectedItem();
        choixUnite2 = (String) spinnerUnite.getSelectedItem();

        if (choixFaction2 == "Choisissez une faction" || choixUnite2 == "Choisissez une unite") {
            Toast.makeText(context, "Choisissez dans tout les champs", Toast.LENGTH_SHORT).show();
        } else {
            ((MainActivity)getActivity()).sendData2(choixFaction2,choixUnite2,choixFaction1,choixUnite1);
        }
    }

    public void spinnerInitialize() {
        factions = new ArrayList<>();
        unites = new ArrayList<>();
        factions.add("Choisissez une faction");
        unites.add("Choisissez une unite");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbfaction = mDatabase.child("factions");
        dbfaction.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    faction = snap.child("nom").getValue().toString();
                    factions.add(faction);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        dbUnite = mDatabase.child("unite");
        dbUnite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    unite = snap.child("nom").getValue().toString();
                    unites.add(unite);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        ArrayAdapter<String> FactionAdapter = new ArrayAdapter<>(context, R.layout.spinner, factions);
        FactionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaction.setAdapter(FactionAdapter);
        ArrayAdapter<String> uniteAdapter = new ArrayAdapter<>(context, R.layout.spinner, unites);
        uniteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnite.setAdapter(uniteAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
