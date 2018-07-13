package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import a2018.warhammer.alex.warhammerbattlesimulator.MainActivity;
import a2018.warhammer.alex.warhammerbattlesimulator.R;


public class CombatFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private String choixFaction2, choixUnite2, choixFaction1, choixUnite1;
    private TextView combat1, combat2;
    private Button combatre;
    private DatabaseReference mDatabase, unite1, unite2;
    private Double attaque1, capaciteCombat1, capaciteTir1, commandement1, cout1, endurance1, force1, mouvement1,
            pv1, sauvegardeinvulnerable1, sauvegardeNormale1, attaque2, capaciteCombat2,
            capaciteTir2, commandement2, cout2, endurance2, force2, mouvement2, pv2, sauvegardeinvulnerable2, sauvegardeNormale2;
    private int round = 0;


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

    public void initialize() {
        Bundle bundle1 = this.getArguments();
        Bundle bundle2 = this.getArguments();
        choixFaction1 = bundle1.getString("faction1");
        choixUnite1 = bundle1.getString("unite1");
        choixFaction2 = bundle2.getString("faction2");
        choixUnite2 = bundle2.getString("unite2");

        combat1 = view.findViewById(R.id.TV_combat_choix1);
        combat2 = view.findViewById(R.id.TV_combat_choix2);
        combatre = view.findViewById(R.id.BTN_combat_suivant);
        combatre.setOnClickListener(this);
    }

    public void setText() {
        combat1.setText(choixFaction1 + " / " + choixUnite1);
        combat2.setText(choixFaction2 + " / " + choixUnite2);
    }

    @Override
    public void onClick(View view) {
        combat();
    }

    public void combat() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        unite1 = mDatabase.child(choixUnite1);
        unite1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    attaque1 = Double.parseDouble(snap.child("attaque").getValue().toString());
                    capaciteCombat1 = Double.parseDouble(snap.child("capaciteCombat").getValue().toString());
                    capaciteTir1 = Double.parseDouble(snap.child("capaciteTir").getValue().toString());
                    commandement1 = Double.parseDouble(snap.child("commandement").getValue().toString());
                    cout1 = Double.parseDouble(snap.child("cout").getValue().toString());
                    endurance1 = Double.parseDouble(snap.child("endurance").getValue().toString());
                    force1 = Double.parseDouble(snap.child("force").getValue().toString());
                    mouvement1 = Double.parseDouble(snap.child("mouvement").getValue().toString());
                    pv1 = Double.parseDouble(snap.child("pv").getValue().toString());
                    sauvegardeinvulnerable1 = Double.parseDouble(snap.child("sauvegardeinvulnerable").getValue().toString());
                    sauvegardeNormale1 = Double.parseDouble(snap.child("sauvegardeNormale").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        unite2 = mDatabase.child(choixUnite2);
        unite2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    attaque2 = Double.parseDouble(snap.child("attaque").getValue().toString());
                    capaciteCombat2 = Double.parseDouble(snap.child("capaciteCombat").getValue().toString());
                    capaciteTir2 = Double.parseDouble(snap.child("capaciteTir").getValue().toString());
                    commandement2 = Double.parseDouble(snap.child("commandement").getValue().toString());
                    cout2 = Double.parseDouble(snap.child("cout").getValue().toString());
                    endurance2 = Double.parseDouble(snap.child("endurance").getValue().toString());
                    force2 = Double.parseDouble(snap.child("force").getValue().toString());
                    mouvement2 = Double.parseDouble(snap.child("mouvement").getValue().toString());
                    pv2 = Double.parseDouble(snap.child("pv").getValue().toString());
                    sauvegardeinvulnerable2 = Double.parseDouble(snap.child("sauvegardeinvulnerable").getValue().toString());
                    sauvegardeNormale2 = Double.parseDouble(snap.child("sauvegardeNormale").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        if (pv1 >= 1) {
            phase1();
            phase2();
        } else {
            finCombat(choixUnite2);
        }

    }

    public void finCombat(String vainqueur) {

        ((MainActivity) getActivity()).sendDataStats(choixUnite1, choixUnite2, vainqueur);
    }

    //todo class combatant avec methode de combat pour les phases

    public void phase1() {

        boolean testTir, testBlessure, testSauvegarde;
        round = round + 1;
        //todo set text round
        Random rn = new Random();
        int random = 1 + (6 - 1) * rn.nextInt();
        if (capaciteTir1 >= random) {
            testTir = true;
            //todo set text touché
            if (force1 > endurance2) {
                random = 1 + (6 - 1) * rn.nextInt();
                if (random >= 3) {
                    //todo set text blessé
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= sauvegardeinvulnerable2) {

                    } else {
                        pv2 -= 1;
                    }
                } else {
                    //todo set text raté
                }
            } else if (force1.equals(endurance2)) {
                random = 1 + (6 - 1) * rn.nextInt();
                if (random >= 4) {
                    //todo set text blessé
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= sauvegardeinvulnerable2) {

                    } else {
                        pv2 -= 1;
                    }
                } else {
                    //todo set text raté
                }
            } else {
                random = 1 + (6 - 1) * rn.nextInt();
                if (random >= 5) {
                    //todo set text blessé
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= sauvegardeinvulnerable2) {

                    } else {
                        pv2 -= 1;
                    }
                } else {
                    //todo set text raté
                }

            }
        } else {
            testTir = false;
            //todo set text raté
        }


    }

    public void phase2() {

        if (pv2 >= 1) {
            boolean testTir, testBlessure, testSauvegarde;
            Random rn = new Random();
            int random = 1 + (6 - 1) * rn.nextInt();
            if (capaciteTir2 >= random) {
                testTir = true;
                //todo set text touché
                if (force2 > endurance1) {
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= 3) {
                        //todo set text blessé
                        random = 1 + (6 - 1) * rn.nextInt();
                        if (random >= sauvegardeinvulnerable1) {

                        } else {
                            pv1 -= 1;
                        }
                    } else {
                        //todo set text raté
                    }
                } else if (force1.equals(endurance1)) {
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= 4) {
                        //todo set text blessé
                        random = 1 + (6 - 1) * rn.nextInt();
                        if (random >= sauvegardeinvulnerable1) {

                        } else {
                            pv1 -= 1;
                        }
                    } else {
                        //todo set text raté
                    }
                } else {
                    random = 1 + (6 - 1) * rn.nextInt();
                    if (random >= 5) {
                        //todo set text blessé
                        random = 1 + (6 - 1) * rn.nextInt();
                        if (random >= sauvegardeinvulnerable1) {

                        } else {
                            pv1 -= 1;
                        }
                    } else {
                        //todo set text raté
                    }

                }
            } else {
                testTir = false;
                //todo set text raté
            }

        } else {
            finCombat(choixUnite1);
        }
    }

}

