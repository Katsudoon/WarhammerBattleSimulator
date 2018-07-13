package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import a2018.warhammer.alex.warhammerbattlesimulator.R;
import a2018.warhammer.alex.warhammerbattlesimulator.services.MusiqueIntentService;

import static android.content.Context.MODE_PRIVATE;


public class ConnexionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private EditText mail, password;
    private FirebaseAuth mAuth;
    private String verifMail, verifPassword;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_MAIL = "Mail";


    public ConnexionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connexion, container, false);
        context = view.getContext();
        initialize();
        return view;
    }

    public void initialize() {

        mail = view.findViewById(R.id.ED_connexion_mail);
        password = view.findViewById(R.id.ED_connexion_password);
        Button valider = view.findViewById(R.id.BTN_connexion_valider);
        valider.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String Email = pref.getString(PREF_MAIL, null);

        if (Email != null) {
            mail.setText(Email);
        }

    }

    @Override
    public void onClick(View v) {
        MusiqueIntentService.pressed(view.getContext());
        verifMail = mail.getText().toString().trim();
        verifPassword = password.getText().toString().trim();

        if (verifMail.equals("") || verifPassword.equals("")) {
            Toast.makeText(context, "Veuillez remplir TOUT les champs", Toast.LENGTH_SHORT).show();
        } else {
            context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_MAIL, verifMail)
                    .apply();
            Connect();
        }

    }

    public void Connect() {
        String user_email = verifMail;
        String user_password = verifPassword;
        mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                GoToMenuFrag();
                } else
                    Toast.makeText(context, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoToMenuFrag() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_accueil, new MenuFragment())
                .commit();
    }

}
