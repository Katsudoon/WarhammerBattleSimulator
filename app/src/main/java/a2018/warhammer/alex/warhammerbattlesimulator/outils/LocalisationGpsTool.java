package a2018.warhammer.alex.warhammerbattlesimulator.outils;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import a2018.warhammer.alex.warhammerbattlesimulator.models.CoordonesModel;


public class LocalisationGpsTool implements LocationListener {

    private LocationManager localisation;


    public LocalisationGpsTool(Context context) {
        this.localisation = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    //region CallBack
    public interface ILocationGPS {
        void onCurrentLocation(CoordonesModel c);
    }

    private ILocationGPS callback;

    public void setCallback(ILocationGPS callback) {
        this.callback = callback;
    }
    //endregion


    // renvoi la position toute les 5 secondes
    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION})
    public void getPosition() throws SecurityException {
        localisation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this, null);
    }

    //fournit au callback la lat et long
    @Override
    public void onLocationChanged(Location location) {

        double lon = location.getLongitude();
        double lat = location.getLatitude();

        CoordonesModel c = new CoordonesModel(lat, lon);

        if (callback != null) {
            callback.onCurrentLocation(c);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
