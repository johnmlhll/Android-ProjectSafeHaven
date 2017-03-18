package com.dbs.jmulhall.projectsafehavenapi25;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewRegistrationPhoto extends AppCompatActivity {

    //declare final var for photo capture
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button photoButton;
    private ImageView imgPhotoBoxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_registration_photo);

        //API >24 Not Allowed - FileProvider Implemented for URI > API 24 support
        //Downgraded MinSDKVersion to API23 to get Camera Implementation using File to work
        //variables declaration
        photoButton = (Button)findViewById(R.id.btnTakePhoto);
        imgPhotoBoxView = (ImageView)findViewById(R.id.imgViewRefugeeApplicantImage);

        //permissions validation
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            photoButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            Toast.makeText(getBaseContext(), "Issue: Camera permissions were re-requested from device", Toast.LENGTH_SHORT).show();
        }

        //Uri level listener for take picture button
        photoButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    //camera methods - file set up
    private File getFile() {
        File imageFolder = new File("sdcard/camera_app");
        if(!imageFolder.exists()) {
            imageFolder.mkdir();
        }
        File imageFile = new File(imageFolder, "camera_pic.jpg");
        return imageFile;
    }
    //Postback method return from listener in onCreate method for Camera Permissions
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imgPhotoBoxView.setImageResource(android.R.color.transparent);
        super.onActivityResult(requestCode, resultCode, data);
        //variables
        String filePath = "sdcard/camera_app/camera_pic.jpg";
        imgPhotoBoxView.setImageDrawable(Drawable.createFromPath(filePath));
    }

        //home button method
        public void toHomeFromNewRegPhoto (View view){
            Intent homeIntentFromNewRegPhoto = new Intent(this, Home.class);
            startActivity(homeIntentFromNewRegPhoto);
        }
        //back button method
        public void goback (View view){
            Intent goBackFromNewRegPhoto = new Intent(this, NewRegistration.class);
            startActivity(goBackFromNewRegPhoto);
        }

    //SaveNotes Method to add to the saved locally personal details
    public void saveNotes(View view){
        //open shared preference instance for data processing
        SharedPreferences personalDetails = this.getSharedPreferences("com.dbs.jmulhall.projectsafehavenapi25", Context.MODE_PRIVATE);
        //declare vars and add case notes to the ArrayList
        EditText caseNoteEntry = (EditText)findViewById(R.id.txtMultiLineCaseNotes);
        NewRegistration.personalDetailsRecord.add(caseNoteEntry.getText().toString());

        //serialise using the objectserializer class and save the ArrayList usins SharedPreferences.personalDetails;
        try {
            personalDetails.edit().putString("personalRecord", ObjectSerializer.serialize(NewRegistration.personalDetailsRecord)).apply();
            Toast.makeText(this, "Succesfully Saved Case Note Entry to Local Storage", Toast.LENGTH_LONG).show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        //deserialise and pass back from SharedPreferences.personalDetails for view/logging/further action/etc
        ArrayList<String> serializedPersonalDetails  = new ArrayList<>();
        try {
            serializedPersonalDetails = (ArrayList<String>)ObjectSerializer.deserialize(personalDetails.getString("personalRecord",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch(IOException e) {
            e.printStackTrace();
        }
        //further actions with the in-mem record retreived from SharedPreferences.personalDetails
        Log.i("Personal Record", serializedPersonalDetails.toString()); //Logging details before passing to DB/View functions
    }
}
