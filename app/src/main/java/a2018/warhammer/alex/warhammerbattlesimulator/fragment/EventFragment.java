package a2018.warhammer.alex.warhammerbattlesimulator.fragment;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import a2018.warhammer.alex.warhammerbattlesimulator.R;
import a2018.warhammer.alex.warhammerbattlesimulator.models.CoordonesModel;
import a2018.warhammer.alex.warhammerbattlesimulator.outils.LocalisationGpsTool;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class EventFragment extends Fragment implements LocalisationGpsTool.ILocationGPS, OnMapReadyCallback, View.OnClickListener {

    private final int PERMISSION_CODE_GPS = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 100;

    private LocalisationGpsTool gps;
    private Context context;
    private GoogleMap gMap;
    private LatLng positionActuelle;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, event, utilisateurs;
    private View view;
    private double latitude, longitude, token, newToken;
    private boolean camera, trouver;


    public EventFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);

        context = view.getContext();
        initialize();
        return view;
    }

    //demande de la position apres toute les verif
    public void checkLocation() {

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_GPS);
            return;
        }

        gps.getPosition();

    }

    //verif gps autorisé (permission)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE_GPS && grantResults[0] == 0) {
            checkLocation();
        } else {
            Toast.makeText(context, "GPS necessaire", Toast.LENGTH_LONG).show();

        }
    }


    // set le marqueur utilisateur toute les 5 secondes
    @Override
    public void onCurrentLocation(CoordonesModel c) {
        if (gMap == null) return;

        positionActuelle = new LatLng(c.getLatitude(), c.getLongitude());
        gMap.clear();
        if (!camera) {
            gMap.moveCamera(CameraUpdateFactory.newLatLng(positionActuelle));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(17f));
            camera = true;
        }
        MarkerOptions utilisateur = (new MarkerOptions()).position(positionActuelle).title("Vous etes ici");
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.marqueur);
        Bitmap iconsmall = Bitmap.createScaledBitmap(icon, 200, 200, false);
        utilisateur.icon(BitmapDescriptorFactory.fromBitmap(iconsmall));
        gMap.addMarker(utilisateur);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        event = mDatabase.child("event");
        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot events : dataSnapshot.getChildren()) {
                    latitude = Double.parseDouble(events.child("latitude").getValue().toString());
                    longitude = Double.parseDouble(events.child("longitude").getValue().toString());
                    String nom = events.child("nom").getValue().toString();

                    LatLng positionEvent = new LatLng(latitude, longitude);
                    MarkerOptions event = (new MarkerOptions()).position(positionEvent).title(nom);
                    gMap.addMarker(event);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }


    public void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        //TODO Switch to new feature => https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        checkLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    //verif GPS activé(sur le telephone)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                checkLocation();
            } else {

                Toast.makeText(context, "GPS is not enabled, cant continue", Toast.LENGTH_LONG).show();
            }

        }
    }

    //initialise la map et les marqueurs events
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    public void initialize() {
        Button check = view.findViewById(R.id.BTN_event_check);
        check.setOnClickListener(this);

        gps = new LocalisationGpsTool(context);
        gps.setCallback(this);

        displayLocationSettingsRequest(context);

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }


    // verification de la position utilisateur par rapport aux events et gain eventuel de token
    public void checkEvent() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        event = mDatabase.child("event");
        utilisateurs = mDatabase.child("utilisateur");
        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot events : dataSnapshot.getChildren()) {
                    latitude = Double.parseDouble(events.child("latitude").getValue().toString());
                    longitude = Double.parseDouble(events.child("longitude").getValue().toString());

                    if (Math.abs(positionActuelle.latitude - latitude) > 0.0000001 && Math.abs(positionActuelle.latitude - latitude) < 0.0002 &&
                            Math.abs(positionActuelle.longitude - longitude) > 0.0000001 && Math.abs(positionActuelle.longitude - longitude) < 0.0002) {
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            utilisateurs.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot utilisateur : dataSnapshot.getChildren()) {
                                        token = Double.parseDouble(utilisateur.child("token").getValue().toString());
                                        newToken = token + 1;
                                    }
                                }

                                //TODO Check event checker, puis set dans DB.

                                @Override
                                public void onCancelled(DatabaseError error) {
                                }
                            });
                            //todo voir pourquoi ce putain de set value remet a 0 avant de fonctionner
                            mDatabase.child("utilisateur").child(uid).child("token").setValue(newToken);

                        }
                        Toast.makeText(context, R.string.eventTrouve, Toast.LENGTH_LONG).show();
                        trouver = true;
                        break;

                    }
                    if (!trouver) {
                        Toast.makeText(context, R.string.pasEvent, Toast.LENGTH_LONG).show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        checkEvent();
    }
}