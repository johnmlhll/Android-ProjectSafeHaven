package com.dbs.jmulhall.projectsafehavenapi25;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UploadRegistrations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_registrations);
        //populate list view with shared reference record pre upload to Firebase
        ListView viewRecord = (ListView) findViewById(R.id.lstViewUploads);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NewRegistration.personalDetailsRecord);
        viewRecord.setAdapter(arrayAdapter);
    }
    //home button
    public void toToHomeFromUploadReg(View view) {
        Intent homeIntendFromUploadReg = new Intent(this, Home.class);
        startActivity(homeIntendFromUploadReg);
    }

    public void uploadRegistration(View view) {
            DatabaseReference mDatabaseUpload = FirebaseDatabase.getInstance().getReference();
            //set Firebasefilekey
            Calendar logDate = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(logDate.getTime());
            String record = formattedDate + "NewRegistrationRecord";
            //map and execute upload
            Map<String, String> uploadRecord = new HashMap<>();
            uploadRecord.put(record, NewRegistration.personalDetailsRecord.toString());
            mDatabaseUpload.push().setValue(uploadRecord, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Log.i("Information", "New Record succesfully Uploaded to Firebase DB");
                        Toast.makeText(getBaseContext(), "New Record succesfully Uploaded to Firebase DB", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("Information", "New Record Uploaded to Firebase DB Failed");
                        Toast.makeText(getBaseContext(), "New Record Uploaded to Firebase DB Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        NewRegistration.personalDetailsRecord.clear(); //clears static arraylist after all updates are inputted and uploaded to Firebase
    }
}
