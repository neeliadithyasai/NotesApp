package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class notesDetailsActivity extends AppCompatActivity {

    TextView notesDetails;
    DatabaseReference notesdata;
    Toolbar toolbar;
    String selectednotelat;
    String selectednotelon;
    String id;
    String sname;

    //firebasestorage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("images");
    String audioname;
    StorageReference imgs;

    //play
    MediaPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        notesDetails = findViewById(R.id.notesDetails);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView nm = (ImageView)findViewById(R.id.noteimage);

        final ImageView play = (ImageView) findViewById(R.id.playAudio);




        Intent i = getIntent();
         id = i.getExtras().getString("id");
        sname = i.getExtras().getString("sname");
        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(sname).child("subjectnotes").child(id);

        Toast.makeText(this,String.valueOf(id), Toast.LENGTH_LONG).show();
        Log.d("detailshow", String.valueOf(notesdata.child(id)));

        notesdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                    notesDetails.setText(dataSnapshot.child("content").getValue().toString());
                    selectednotelat = dataSnapshot.child("userlat").getValue().toString();
                    selectednotelon = dataSnapshot.child("userlong").getValue().toString();
                    if(dataSnapshot.child("filename").getValue() != null) {
                        audioname = dataSnapshot.child("filename").getValue().toString();
                    }else
                    {
                        play.setClickable(false);
                    }
                    getSupportActionBar().setTitle(dataSnapshot.child("title").getValue().toString());

                    Log.d("showmedata", dataSnapshot.child("title").getValue().toString());

                if (dataSnapshot.child("imagename").getValue() != null) {
                    try {
                        final File file = File.createTempFile("image", "jpg");

                        imgs = storageReference.child(dataSnapshot.child("imagename").getValue().toString());


                        imgs.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                nm.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(notesDetailsActivity.this, "no image found", Toast.LENGTH_LONG).show();

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == event.ACTION_DOWN){
                    startPlaying();


                }else if(event.getAction() == event.ACTION_UP){
                    stopPlaying();


                }

                return false;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_edit_menu,menu);
        return true;


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mapsmenu){
            Intent i = new Intent(this, mapsActivity.class);
           i.putExtra("notelat",selectednotelat);
            i.putExtra("notelon",selectednotelon);
            startActivity(i);

        }
        if(item.getItemId() == R.id.save_editednotes){

            notesdata.child("content").setValue(notesDetails.getText().toString());
            onBackPressed();


        }






        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
          player.setDataSource(String.valueOf(audioname));
           player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
               @Override
               public void onPrepared(MediaPlayer mp) {
                   player.start();
               }
           });

           player.prepare();

        } catch (IOException e) {
            Log.e("audioplay", "prepare() failed"+audioname);
        }
    }


    private void stopPlaying() {
        player.release();
        player = null;
    }
}
