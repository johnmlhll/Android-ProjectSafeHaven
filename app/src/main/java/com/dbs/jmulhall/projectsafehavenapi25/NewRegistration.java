package com.dbs.jmulhall.projectsafehavenapi25;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_registration);
    }
    //Method 1 - Home Button goto
    public void goToHomeFromNewReg(View view) {
        Intent homeFromNewReg = new Intent(this, Home.class);
        startActivity(homeFromNewReg);
    }
    //Method 2 - Next Page Button goto
    public void toNewRegistrationPhoto(View view) {
        Intent newRegisrationPhotoIntend = new Intent(this, NewRegistrationPhoto.class);
        startActivity(newRegisrationPhotoIntend);
    }

    //method 3 - Pass address deatils to Google Maps
    public void checkAddress(View view) {
        try {
            //form vars for address
            EditText streetAddress = (EditText) findViewById(R.id.txtAddressLine1);
            //string vars for passing to maps
            String streetAddressString = streetAddress.getText().toString();
            Toast.makeText(this, "AddressLine1: "+streetAddressString, Toast.LENGTH_LONG).show(); //TEST
            MapsActivity checkNewAddressOnMap = new MapsActivity();
            checkNewAddressOnMap.checkAddress(streetAddressString);
        }
        catch (NullPointerException ne) {
            Toast.makeText(this, "No Location Found or Empty Fields passed to Google Maps, please retry", Toast.LENGTH_SHORT).show();
            ne.printStackTrace();
        }
    }

    //instantiate array and add fields for serialization/saving accross the two New Registration Activities
    static ArrayList<String> personalDetailsRecord = new ArrayList<>();

    //Method 4 -save regsistration information using shared prefereneces
    public void savePersonalDetails(View view) {
        //open shared preference instance for data processing
        SharedPreferences personalDetails = this.getSharedPreferences("com.dbs.jmulhall.projectsafehavenapi25", Context.MODE_PRIVATE);
        //declare and initalise field vars
        EditText givenName = (EditText)findViewById(R.id.txtGivenName);
        EditText firstName = (EditText) findViewById(R.id.txtFirstName);
        EditText lastName = (EditText) findViewById(R.id.txtLastName);
        EditText gender =  (EditText) findViewById(R.id.txtGender);
        EditText dateOfBirth = (EditText) findViewById(R.id.txtDateOfBirth);
        EditText citizenship = (EditText) findViewById(R.id.txtCitizenship);
        EditText streetAddress = (EditText) findViewById(R.id.txtAddressLine1);
        EditText area = (EditText) findViewById(R.id.txtTownArea);
        EditText city = (EditText) findViewById(R.id.txtCity);
        EditText postCode = (EditText) findViewById(R.id.txtPostCode);
        EditText country = (EditText) findViewById(R.id.txtCountry);
        //Assign values to the static array
        //personalDetailsRecord.clear();
        personalDetailsRecord.add(givenName.getText().toString());
        personalDetailsRecord.add(firstName.getText().toString());
        personalDetailsRecord.add(lastName.getText().toString());
        personalDetailsRecord.add(gender.getText().toString());
        personalDetailsRecord.add(dateOfBirth.getText().toString());
        personalDetailsRecord.add(citizenship.getText().toString());
        personalDetailsRecord.add(streetAddress.getText().toString());
        personalDetailsRecord.add(area.getText().toString());
        personalDetailsRecord.add(city.getText().toString());
        personalDetailsRecord.add(postCode.getText().toString());
        personalDetailsRecord.add(country.getText().toString());
        //feedback to user
        Toast.makeText(this, "Succesfully Saved Record to Local Storage", Toast.LENGTH_LONG).show();
    }
}
