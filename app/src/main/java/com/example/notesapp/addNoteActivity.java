package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.model.notes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class addNoteActivity extends AppCompatActivity {

    //database variables intialization
    DatabaseReference notesdata;
    notes Notes;
    long maxId;
    StorageReference mref;

    //view variable initialization
    Toolbar toolbar;
    EditText notesTitle, noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    String id;
    double noteslat;
    double noteslong;
    LatLng coli;


   //varibles for image and audio
    Button ch;
    Button audioup;
    ImageView uploadingimage;
     public Uri imguri;
     private StorageTask uploadTask;
      String imagefilename;
      private MediaRecorder recorder;
      private String fileName = null;
      private static final String LOG_TAG = "recordlog";
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ProgressDialog mprogress;
    private StorageTask audiotask;
    private boolean imagepresent = false;


    //initialization for location.
    FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //initialization of views.
        notesTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        client = LocationServices.getFusedLocationProviderClient(this);
        mref = FirebaseStorage.getInstance().getReference("images");
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        if (ActivityCompat.checkSelfPermission(addNoteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getcurrentLoaction();

        } else {
            ActivityCompat.requestPermissions(addNoteActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        Intent i = getIntent();
        id = i.getExtras().getString("subname");

        ch = (Button) findViewById(R.id.chooseimage);
        audioup = (Button) findViewById(R.id.uploadimage);
        final TextView auidolabel = (TextView) findViewById(R.id.audiotxt);
        uploadingimage = (ImageView)  findViewById(R.id.uploadingImage);

        fileName =getExternalCacheDir().getAbsolutePath();
        fileName += "/"+System.currentTimeMillis()+".3gp";
        fileName.replaceAll(" ", "_");
     // fileName = String.valueOf(System.currentTimeMillis());

        mprogress = new ProgressDialog(this);


        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();

            }
        });

        audioup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == event.ACTION_DOWN){
                    startRecording();
                    auidolabel.setText("recording");

                }else if(event.getAction() == event.ACTION_UP){
                    stopRecording();
                    auidolabel.setText("recording stopped");

                }

                return false;
            }
        });


        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(id).child("subjectnotes");
        notesdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    maxId = (dataSnapshot.getChildrenCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //noteTitle functions.
        notesTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("calander", "Date and time" + todaysDate + " and " + currentTime);
    }

    private void getcurrentLoaction() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> Task = client.getLastLocation();
        Task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location != null){


                    noteslat = location.getLatitude();
                    noteslong = location.getLongitude();

                    coli = new LatLng(location.getLatitude(),location.getLongitude());


                    Log.d("userlocation", String.valueOf(location.getLatitude()));





                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getcurrentLoaction();
            }
        }

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    //pad for time
    private String pad(int i) {

        if(i<10)
            return "0"+i;

          return   String.valueOf(i);

    }

    private  void Filechooser(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode ==RESULT_OK && data!= null && data.getData() != null){

            imguri = data.getData();
            uploadingimage.setImageURI(imguri);
            imagepresent = true;


        }
    }

    private String getExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }

    private  void Fileuploader(){



        imagefilename = System.currentTimeMillis()+"."+getExtension(imguri);
        StorageReference ref = mref.child(imagefilename);

        uploadTask = ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Toast.makeText(addNoteActivity.this,"imageupload successfull",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    //code for options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete){
            Toast.makeText(this,"note not saved",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save){


            Notes = new notes(String.valueOf(maxId+1),notesTitle.getText().toString(),noteDetails.getText().toString(),todaysDate,currentTime,String.valueOf(id),noteslat,noteslong);
            //notesdata.child("1").setValue(Notes);

             notesdata.child(String.valueOf(maxId+1)).setValue(Notes);

           if(imagepresent == true) {
               Fileuploader();
               notesdata.child(String.valueOf(maxId + 1)).child("imagename").setValue(String.valueOf(imagefilename));
           }else {
               notesdata.child(String.valueOf(maxId + 1)).child("imagename").setValue("noimage");
           }
                notesdata.child(String.valueOf(maxId+1)).child("filename").setValue(String.valueOf(fileName));
            notesdata.child(String.valueOf(maxId+1)).child("mulmaps").setValue("0");


            Toast.makeText(this,"save button is clicked",Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

            mprogress.setMessage("uploading audio");
            mprogress.show();
            uploadaudio();

            mprogress.dismiss();



    }

    private void uploadaudio(){

        Toast.makeText(addNoteActivity.this,"uploading audio",Toast.LENGTH_LONG).show();
        StorageReference aref = FirebaseStorage.getInstance().getReference("audio").child(fileName);
        Uri uri = Uri.fromFile(new File(fileName));

        audiotask = aref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Toast.makeText(addNoteActivity.this,"audio upload successfull",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }
}
