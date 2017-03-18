package com.dbs.jmulhall.projectsafehavenapi25;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //vars needed call location manager (below) for user location
    LocationManager locationManager;
    LocationListener locationListener;

    //check for permission request ok from user for use for maps listen for location


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    //calls the map asynchronisally
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //sets initial coordiantes (Damascus, Syra), marker settings and Zoom level for map use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //sets map type for address checking
        //call user location for maps routine
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // User location programmed as centroid location for Project SafeHaven
                LatLng userlocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userlocation).title("Your Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 8));
                //Get geo coded (physical) addresses from Google API
                Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address>listAddresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(listAddresses == null && listAddresses.size() > 0) {
                        Log.i("Place Information: ", listAddresses.get(0).toString());
                        Toast.makeText(getBaseContext(), listAddresses.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch(IOException io) {
                    io.printStackTrace();
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
        };
        //check if <API23 and get user location
        if(Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {
            try {
                //check for user permission (>=API23) to use their location or/else perform user location check with existing permissions
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    //get last known location for zooming in purposes when app is launched
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.clear(); //clears lat/long points before they are updated
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Current Location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 8));
                }
            }
            catch(NullPointerException ne)
            {
                LatLng userLocation = new LatLng(53.3472612, -6.2609718);
                Toast.makeText(getBaseContext(), "GPS Coordinates Missing... \nUsed Default Coordinates", Toast.LENGTH_LONG).show();
            }
        }
    }
    //look for street address and point to it on Google amps.
    public void checkAddress(String streetAddress) {
        try {
            Uri checkAddressURI = Uri.parse(streetAddress);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, checkAddressURI);
            mapIntent.setPackage("com.dbs.jmulhall.projectsafehavenapi25");
            startActivity(mapIntent);
        }
        catch(NullPointerException ne) {
            Toast.makeText(getBaseContext(), "Street Name Not Found on Google Maps", Toast.LENGTH_LONG).show();
        }
    }
}
