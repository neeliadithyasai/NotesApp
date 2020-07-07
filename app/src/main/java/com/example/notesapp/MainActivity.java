package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //database variables intialization
    DatabaseReference notesdata;
    notes Notes;
    long maxId;
    List<notes> notesList = new ArrayList<>();
    adapterNotes allDataAdapter;
    String id;

    //view variable initialization
    Toolbar toolbar;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"connection success",Toast.LENGTH_LONG).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child("science").child("subjectnotes");


        Intent i = getIntent();
      id = i.getExtras().getString("subname");
        notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(id).child("subjectnotes");



        notesdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    notes note = dataSnapshot1.getValue(notes.class);
                    notesList.add(note);

                }
//                Log.d("showmedata",String.valueOf(dataSnapshot.getValue(notes.class)));


                allDataAdapter = new adapterNotes(MainActivity.this, notesList);
                recyclerView.setAdapter(allDataAdapter);
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
            inflater.inflate(R.menu.add_menu,menu);
            return true;


            }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add){
            Intent i = new Intent(this, addNote.class);
            i.putExtra("subname",String.valueOf(id));
            startActivity(i);

            Toast.makeText(this,"Add button is clicked",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
