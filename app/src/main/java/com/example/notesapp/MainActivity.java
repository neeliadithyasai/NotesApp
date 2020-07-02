package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.EventListener;

public class MainActivity extends AppCompatActivity {
    //database variables intialization
    DatabaseReference notesdata;
    notes Notes;
    long maxId;

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
            startActivity(i);
            Toast.makeText(this,"Add button is clicked",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
