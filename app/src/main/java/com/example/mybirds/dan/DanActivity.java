package com.example.mybirds.dan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybirds.R;

import java.util.List;

public class DanActivity extends AppCompatActivity {

    private final String[] REQUIRED_PERMISSIONS_LOCALISATION = new String[]{"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    private final int REQUEST_CODE_PERMISSIONS_LOCALISATION = 222;
    LocationManager locationManager = null;
    private String locationProvider;
    private String latitudeText;
    private String longitudeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dan);

        Button birdListButton = (Button) findViewById(R.id.ToBirdList);
        Button getPositionButton = (Button) findViewById(R.id.showCoordinate);
        birdListButton.setOnClickListener(v -> startActivity(new Intent(DanActivity.this, WikipediaFragment.class)));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = "";
        if (allPermissionsGranted()) {
            initialiserLocalisation();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS_LOCALISATION, REQUEST_CODE_PERMISSIONS_LOCALISATION);
        }
        getPositionButton.setOnClickListener(v -> ((TextView) findViewById(R.id.coordinate)).
                setText("Latitude : "+latitudeText+" et longitude : "+longitudeText));
    }

    private void initialiserLocalisation() {
        System.out.println("location manager : "+locationManager);
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            System.out.println("location manager : " + locationManager);
            if (locationManager == null) {
                Toast.makeText(this, "Manager nul", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }

        Criteria critere = new Criteria();

        // Pour indiquer la précision voulue
        critere.setAccuracy(Criteria.ACCURACY_FINE);

        // Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(false);

        // Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(false);

        // Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);

        // Pour indiquer la consommation d'énergie demandée
        critere.setPowerRequirement(Criteria.POWER_LOW);

        // Est-ce que le fournisseur doit être capable de donner une vitesse ?
        critere.setSpeedRequired(false);

        locationProvider = locationManager.getBestProvider(critere, true);
        if(locationProvider.isEmpty()){
            List<String> providers = locationManager.getProviders(critere, true);
            if(providers.isEmpty()){
                for(String nameProvider : locationManager.getAllProviders()){
                    if(!locationManager.getProvider(nameProvider).hasMonetaryCost()){
                        locationProvider = nameProvider;
                        break;
                    }
                }
                if(locationProvider.isEmpty())
                    throw new RuntimeException("Aucun fournisseru disponible");
            }
            else{
                locationProvider = providers.get(0);
            }
        }

        if (!locationProvider.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS_LOCALISATION, REQUEST_CODE_PERMISSIONS_LOCALISATION);
            }

            locationManager.requestLocationUpdates(locationProvider, 60000, 150, new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onLocationChanged(Location location) {
                    latitudeText = Double.toString(location.getLatitude());
                    longitudeText = Double.toString(location.getLongitude());
                }
            });

        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS_LOCALISATION) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_LOCALISATION) {
            if (allPermissionsGranted()) {
                initialiserLocalisation();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}