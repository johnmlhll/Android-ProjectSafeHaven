package com.dbs.jmulhall.projectsafehavenapi25;

        import android.*;
        import android.Manifest;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.database.sqlite.SQLiteDatabase;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.content.Intent;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.HashMap;
        import java.util.Map;

public class Home extends  AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    //button menu methods
    public void goToNewRegistrations(View view) {
        Intent intendNewReg = new Intent(this, NewRegistration.class);
        startActivity(intendNewReg);
    }

    public void gotoViewRegistrations(View view) {
        Intent intentViewReg = new Intent(this, ViewRegistrations.class);
        startActivity(intentViewReg);
    }

    public void goToUploadRegistrations(View view) {
        Intent intendUploadReg = new Intent(this, UploadRegistrations.class);
        startActivity(intendUploadReg);
    }
    public void goToMap(View view) {
        Intent intendMaps = new Intent(this, MapsActivity.class);
        startActivity(intendMaps);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //kill session login with firebase
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        Toast.makeText(getBaseContext(), "You are now logged out", Toast.LENGTH_LONG).show();
    }

    //maybe a logout button?
}