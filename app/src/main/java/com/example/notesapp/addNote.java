package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class addNote extends AppCompatActivity {

    //database variables intialization
    DatabaseReference notesdata;
    notes Notes;
    long maxId;

    //view variable initialization
    Toolbar toolbar;
    EditText notesTitle,noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //initialization of views.
        notesTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails );
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        id = i.getExtras().getString("subname");


        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(id).child("subjectnotes");
        notesdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

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
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("calander", "Date and time"+todaysDate+" and "+currentTime);
    }

    //pad for time
    private String pad(int i) {

        if(i<10)
            return "0"+i;

          return   String.valueOf(i);

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


            Notes = new notes(String.valueOf(maxId+1),notesTitle.getText().toString(),noteDetails.getText().toString(),todaysDate,currentTime,String.valueOf(id));
            //notesdata.child("1").setValue(Notes);

             notesdata.child(String.valueOf(maxId+1)).setValue(Notes);
            Toast.makeText(this,"save button is clicked",Toast.LENGTH_LONG).show();
          onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
