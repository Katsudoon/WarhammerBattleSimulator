package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import a2018.warhammer.alex.warhammerbattlesimulator.R;
import a2018.warhammer.alex.warhammerbattlesimulator.models.UtilisateurModel;
import a2018.warhammer.alex.warhammerbattlesimulator.services.MusiqueIntentService;


public class InscriptionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context context;
    private EditText mail, password;
    private FirebaseAuth mAuth;
    private String verifMail, verifPassword;


    public InscriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inscription, container, false);
        context = view.getContext();
        initialize();
        return view;
    }

    public void initialize() {

        mail = view.findViewById(R.id.ED_inscription_mail);
        password = view.findViewById(R.id.ED_inscription_password);
        Button valider = view.findViewById(R.id.BTN_inscription_valider);
        valider.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        MusiqueIntentService.pressed(view.getContext());
        verifMail = mail.getText().toString().trim();
        verifPassword = password.getText().toString().trim();

        if (verifMail.equals("") || verifPassword.equals("")) {
            Toast.makeText(context, "Veuillez remplir TOUT les champs", Toast.LENGTH_SHORT).show();
        } else {
            createAccount();
        }

    }

    public void createAccount() {
        String user_email = verifMail;
        String user_password = verifPassword;
        mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                } else
                    Toast.makeText(context, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendEmailVerification() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Vous êtes bien enregistré, un Email de vérification vous a été envoyé", Toast.LENGTH_LONG).show();
                        String Uid = firebaseUser.getUid();
                        nouvelUtilisateur(Uid, verifMail, verifPassword);
                        mAuth.signOut();
                        GoToMenuFrag();
                    } else {
                        Toast.makeText(context, "L'Email de vérification n'a pas pu être envoyé", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void GoToMenuFrag() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_accueil, new MenuFragment())
                .commit();
    }

    private void nouvelUtilisateur(String uid, String mail, String password) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        UtilisateurModel utilisateur = new UtilisateurModel(mail, password, 10);

        database.child("utilisateur").child(uid).setValue(utilisateur);
    }

}
