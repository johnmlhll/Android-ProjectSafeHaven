package com.dbs.jmulhall.projectsafehavenapi25;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class ViewRegistrations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registration);
    }

    //home button
    public void goToHomeFromViewReg(View view) {
        Intent homeIntendFromViewRegs = new Intent(this, Home.class);
        startActivity(homeIntendFromViewRegs);
    }

    protected void displayLastRecord(View view) {
        //populate list view with shared reference record
        ListView viewRecord = (ListView) findViewById(R.id.lstViewLastRecordView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NewRegistration.personalDetailsRecord);
        viewRecord.setAdapter(arrayAdapter);

        //add recorded image of the refugee status applicant relating to the record

        ImageView imgPhotoBoxView = (ImageView)findViewById(R.id.imgViewRegistrationApplicant);
        //File imageFolder = new File("sdcard/camera_app");
        //File imageFile = new File(imageFolder, "camera_pic.jpg");
        String filePath = "sdcard/camera_app/camera_pic.jpg";
        imgPhotoBoxView.setImageDrawable(Drawable.createFromPath(filePath));

        /*
        File sd = Environment.getExternalStorageDirectory();
        File imageFile = new File(sd+"sdcard/camera_app/", "camera_pic.jpg");
        if(imageFile.exists()) {
                Bitmap recordImage = BitmapFactory.decodeFile(imageFile.getPath());
                ImageView viewRecordImage = (ImageView) findViewById(R.id.imgViewRefugeeApplicantImage);
                viewRecordImage.setImageBitmap(recordImage);
        }
        */
    }


}

