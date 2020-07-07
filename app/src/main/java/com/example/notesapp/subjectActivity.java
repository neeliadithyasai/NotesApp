package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class subjectActivity extends AppCompatActivity {

    //database variables intialization
    DatabaseReference subdata;
    subject Subject;
    long maxId;
    List<subject> sublist = new ArrayList<>();
    adapterSubject allDataAdapter;

    //view variable initialization
    Toolbar toolbar;
    RecyclerView subrecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        subrecyclerView=findViewById(R.id.subjectRecyclerView);
        subrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subdata = FirebaseDatabase.getInstance().getReference().child("Notes");

//        notes scn = new notes("1","rocketscience","new theory","7/7/20","6:30","science");
//
//         Subject  = new subject("science");
//            Subject.addnotes(scn);
//
//
//         subdata.child("science").setValue(Subject);



        subdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sublist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("subjectlog", String.valueOf(dataSnapshot1.getValue(subject.class)));
                    subject subj = dataSnapshot1.getValue(subject.class);
                    sublist.add(subj);

                }

                allDataAdapter = new adapterSubject(subjectActivity.this, sublist);
                subrecyclerView.setAdapter(allDataAdapter);
                allDataAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_subjecct,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addSubject){


            Toast.makeText(this,"Add subject button is clicked",Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Subject Title");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Subject  = new subject(input.getText().toString());
                    subdata.child(input.getText().toString()).setValue(Subject);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}