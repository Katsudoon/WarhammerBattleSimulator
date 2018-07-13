package a2018.warhammer.alex.warhammerbattlesimulator;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import a2018.warhammer.alex.warhammerbattlesimulator.fragment.AccueilFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.fragment.CombatChoixDeuxFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.fragment.CombatChoixUnFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.fragment.CombatFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.fragment.EventFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.fragment.StatistiqueCombatFragment;
import a2018.warhammer.alex.warhammerbattlesimulator.services.MusiqueIntentService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void setFragAccueil() {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.FL_accueil, new AccueilFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    public void playMusic() {
        MusiqueIntentService.start(this);
    }

    public void initialize() {

        setFragAccueil();
        playMusic();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getFragmentManager().findFragmentById(R.id.FL_accueil);
        if (frg != null) {
            frg.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void sendData1(String choixFaction, String choixUnite) {

        Fragment fragment = new CombatChoixDeuxFragment();
        Bundle data = new Bundle();
        data.putString("faction1", choixFaction);
        data.putString("unite1", choixUnite);
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_accueil, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void sendData2(String choixFaction2, String choixUnite2, String choixFaction1, String choixUnite1) {

        Fragment fragment = new CombatFragment();
        Bundle data = new Bundle();
        data.putString("faction2", choixFaction2);
        data.putString("unite2", choixUnite2);
        data.putString("faction1", choixFaction1);
        data.putString("unite1", choixUnite1);
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_accueil, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void sendDataStats(String choixUnite1, String choixUnite2, String vainqueur) {

        Fragment fragment = new StatistiqueCombatFragment();
        Bundle data = new Bundle();
        data.putString("unite2", choixUnite2);
        data.putString("unite1", choixUnite1);
        data.putString("vainqueur",vainqueur );
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_accueil, fragment)
                .addToBackStack(null)
                .commit();

    }

}
